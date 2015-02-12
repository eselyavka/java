package tv.okko.devops.zookeeaper;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.CreateMode;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class WatcherExample implements Watcher {
    ZooKeeper zk = null;
    String hostPort;
    Random random = new Random();
    String serverId = Integer.toHexString(random.nextInt());
    
    WatcherExample(String hostPort) {
        this.hostPort = hostPort;
    }
  
    void startZK() throws Exception {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    void closeZK() throws Exception {
        if (zk == null) {
            throw new NullPointerException(" :: Variable 'zk' was null inside method closeZK."); 
        } else {
            zk.close();
        }
    }

    void createElement() throws Exception {
        zk.create("/testaa", serverId.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    public void process(WatchedEvent e) {
        System.out.println(e);
    }

    public static void main( String[] args ) throws Exception {
        WatcherExample we = new WatcherExample(args[0]);
        we.startZK();
        we.createElement();
        TimeUnit.SECONDS.sleep(10);
        we.closeZK();
    }
}
