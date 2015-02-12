package tv.okko.devops.hive;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

public class HiveManager {
  private static final String driverName = "org.apache.hive.jdbc.HiveDriver";
  private static final Logger LOG = Logger.getLogger(HiveManager.class.getName());
  /**
   * @param args
   * @throws SQLException
   */
  public static void main(String[] args) throws SQLException {
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (args.length < 1 ) {
        System.out.println("Usage: hive-manager <ip> [dbname] [user] [password] [port]");
        System.exit(1);
    }

    String hostname = toAddress(args[0]);
    String dbname = "default";
    try {
        dbname = args[1];
    } catch (ArrayIndexOutOfBoundsException e ) {
        LOG.debug("[dbname] argument not specified, using default - default");
    }
    String hiveUser = "hive";
    try {
        hiveUser = args[2];
    } catch (ArrayIndexOutOfBoundsException e) {
        LOG.debug("[user] argument not specified, using default - hive");
    }
    String hivePassword = "hive";
    try {
        hivePassword = args[3];
    } catch (ArrayIndexOutOfBoundsException e) {
        LOG.debug("[password] argument not specified, using default - hive");
    }
    Integer port = 10000;
    try {
        port = Integer.parseInt(args[4]);
    } catch (NumberFormatException e) {
        System.err.println("Port " + args[4] + " is not a digit");
        System.exit(2);
    } catch (ArrayIndexOutOfBoundsException e) {
        LOG.debug("[port] argument not specified, using default - 10000");
    }

    Connection con = DriverManager.getConnection("jdbc:hive2://" + hostname + ":" + port + "/" + dbname, hiveUser, hivePassword);
    Statement stmt = con.createStatement();
    String sql = "show tables";
    ResultSet res = stmt.executeQuery(sql);
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", new Locale("us", "US"));
    Date currDate = new Date();
    String tableName = null;
    while (res.next()) {
        tableName = res.getString(1);
        if (tableName != null && tableName.matches("[0-9a-z]{32}")) {
            sql = "describe formatted " + tableName;
            Statement stmt2 = con.createStatement();
            ResultSet res2 = stmt2.executeQuery(sql);
            while(res2.next()) {
                if (res2.getString(1).trim().equalsIgnoreCase("CreateTime:")) {
                    try {
                        Date date = formatter.parse(res2.getString(2));
                        long diff = currDate.getTime() - date.getTime();
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        if (diffDays == 0) {
                            LOG.info("table " + tableName + " created today, nothing to do");
                        } else if (diffDays >= 1) {
                            Statement stmt3 = con.createStatement();
                            stmt3.executeUpdate("drop table " + tableName);
                            stmt3.close();
                            LOG.info("deleting table " + tableName);
                        } else {
                            LOG.warn("table " + tableName + " created in future, something strange");
                        }
                    } catch (ParseException e) {
                        LOG.debug("Can't parse create time field " + res2.getString(2) + " incorrect formatted");
                    }
                }
            }
            stmt2.close();
            res2.close();
        } else {
            LOG.debug("No tables pattern found in database " + dbname);
        }
  }
  res.close();
  stmt.close();
  con.close();
}

  private static String toAddress(String name) {
        try {
            return InetAddress.getByName(name).getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("Can't resolve host name " + name + ", check DNS or host name is correct");
            throw new RuntimeException(e);
        }
    }

}