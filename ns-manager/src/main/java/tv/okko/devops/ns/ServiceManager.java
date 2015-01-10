package tv.okko.devops.ns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.citrix.netscaler.nitro.exception.nitro_exception;
import com.citrix.netscaler.nitro.resource.config.basic.server.gracefulEnum;
import com.citrix.netscaler.nitro.resource.config.basic.service;
import com.citrix.netscaler.nitro.resource.config.basic.service.monitor_stateEnum;
import com.citrix.netscaler.nitro.service.nitro_service;

public class ServiceManager {

    private static final int MAX_AWAIT_TIME = 90000; //max await time in ms.
    private final Options options;
    private final nitro_service nitro;
    private static final Logger LOG = Logger.getLogger(ServiceManager.class.getName());

    public ServiceManager(Options options) throws nitro_exception {
        this.options = options;
        this.nitro = new nitro_service(options.getIp(), "HTTP");
    }

    public void disableService() throws Exception {
        new Callback() {
            @Override
            public void doExecute(service srv) throws Exception {
                String svrstate = srv.get_svrstate();
                switch (svrstate) {
                    case "UP":
                        disable(srv, 90);
                        awaitStatus(srv, monitor_stateEnum.OUT_OF_SERVICE, monitor_stateEnum.DOWN);
                        break;
                    case "OUT OF SERVICE":
                    case "DOWN":
                        LOG.warn("Service " + srv.get_name() + " already in " + svrstate + " state, nothing to do");
                        break;
                    default:
                        String message = "Unknown service " + svrstate + " state type, should be " + monitor_stateEnum.UP;
                        LOG.warn(message);
                        throw new RuntimeException(message);
                }
            }
        }.execute();
    }

    private void awaitStatus(service srv, String... states) throws Exception {
        Set<String> terminalStates = new HashSet<>(Arrays.asList(states));

        long time = System.currentTimeMillis();
        while (true) {
            srv = service.get(nitro, srv.get_name());
            String state = srv.get_svrstate();
            if (terminalStates.contains(state)) {
                LOG.debug("Service " + srv.get_name() + " is now " + state);
                break;
            } else if (System.currentTimeMillis() - time > MAX_AWAIT_TIME) {
                LOG.debug("Service " + srv.get_name() + " status undefined due to a timeout");
                break;
            } else {
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    private void disable(service srv, long timeout) throws Exception {
        srv.set_graceful(gracefulEnum.YES);
        srv.set_delay(timeout);
        service.disable(nitro, srv);
    }

    public void enableService() throws Exception {
        new Callback() {
            @Override
            public void doExecute(service srv) throws Exception {
                switch (srv.get_svrstate()) {
                    case "UP":
                        LOG.debug("Service " + srv.get_name() + " already in UP state, nothing to do");
                        break;
                    case "OUT OF SERVICE":
                    case "DOWN":
                        service.enable(nitro, srv);
                        awaitStatus(srv, monitor_stateEnum.UP);
                        break;
                    default:
                        String message = "Unknown service " + srv.get_name() + " state type, should be " + monitor_stateEnum.OUT_OF_SERVICE + " or " +
                                monitor_stateEnum.DOWN + " state";
                        LOG.warn(message);
                        throw new RuntimeException(message);
                }
            }
        }.execute();
    }

    public service[] getServices() throws Exception {
        String fqdn = options.getServerName();
        if (fqdn == null) {
            throw new NullPointerException(" :: Variable 'fqdn' was null inside method getServices.");
        } else {
            String host = fqdn.split("\\.")[0];
            int port = options.getPort();

            service[] services = service.get_filtered(this.nitro, "servername:" + host + ",port:" + port);
            if (services == null || services.length == 0) {
                LOG.debug("No matches found in NS services with server name: " + host + ", trying " + fqdn);
                services = service.get_filtered(this.nitro, "servername:" + fqdn + ",port:" + port);
                if (services == null || services.length == 0) {
                    String addr = toAddress(fqdn);
                    LOG.debug("No matches found in NS services with server name: " + fqdn + ", trying " + addr);
                    services = service.get_filtered(this.nitro, "servername:" + addr + ",port:" + port);
                    if (services == null || services.length == 0) {
                        LOG.debug("Can't find service by server name " + fqdn + " with corresponding port " + port + " in NS config, nothing to do");
                        return null;
                    }
                }
            }

            return services;
        }
    }

    private static String toAddress(String name) {
        try {
            return InetAddress.getByName(name).getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("Can't resolve host name " + name + ", check DNS or host name is correct");
            throw new RuntimeException(e);
        }
    }

    private abstract class Callback {
        abstract void doExecute(service srv) throws Exception;

        public void execute() throws Exception {
            try {
                nitro.login(options.getUsername(), options.getPassword());

                service[] services = getServices();
                if (services != null) {
                    doExecute(services[0]);
                }
            } finally {
                try {
                    nitro.logout();
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }
}
