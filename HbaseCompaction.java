import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.log4j.Logger;
import java.io.IOException;

public class HbaseCompaction {
  private static final Logger log = Logger.getLogger(HbaseCompaction.class.getName());
  public static void main(String[] args) throws IOException {
    Configuration conf = HBaseConfiguration.create();
    HBaseAdmin hbase = null;

    try {
      hbase = new HBaseAdmin(conf);
    } catch (MasterNotRunningException hm) {
      log.error("Master is not running "+ hm);
      System.exit(1);
    } catch (ZooKeeperConnectionException zk) {
      log.error("Can't connect to zookeeper ensemble " + zk);
      System.exit(2);
    }

    try {
      hbase.checkHBaseAvailable(conf);
      HTableDescriptor[] table = hbase.listTables();
    
      for (int i=0; i<table.length; i++){
        try {
          hbase.isTableAvailable(table[i].getNameAsString());
          try {
            log.info("Starting majorCompact on table: " + table[i].getNameAsString());
            hbase.majorCompact(table[i].getNameAsString());
          } catch (IOException mce) {
            log.error("Can't complete majorCompaction on table: " + table[i].getNameAsString() + mce);
          }
        } catch (IOException ae){
          log.error("Can't access to table " + ae);
        }
      }
    } catch (Exception hae) {
      log.error("Hbase is unavailable " + hae);
    }
  }
}