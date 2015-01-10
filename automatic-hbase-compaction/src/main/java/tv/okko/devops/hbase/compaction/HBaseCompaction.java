package tv.okko.devops.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.log4j.Logger;

public class HBaseCompaction {
    private static final Logger LOG = Logger.getLogger(HBaseCompaction.class.getName());

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseAdmin hbase = null;

        try {
            hbase = new HBaseAdmin(conf);
        } catch (MasterNotRunningException hm) {
            LOG.error("Master is not running " + hm);
            System.exit(1);
        } catch (ZooKeeperConnectionException zk) {
            LOG.error("Can't connect to zookeeper ensemble " + zk);
            System.exit(2);
        }

        try {
            hbase.checkHBaseAvailable(conf);
            HTableDescriptor[] table = hbase.listTables();

            for (int i = 0; i < table.length; i++) {
                try {
                    hbase.isTableAvailable(table[i].getNameAsString());
                    try {
                        LOG.info("Starting majorCompact on table: " + table[i].getNameAsString());
                        hbase.majorCompact(table[i].getNameAsString());
                    } catch (IOException mce) {
                        LOG.error("Can't complete majorCompaction on table: " + table[i].getNameAsString() + mce);
                    }
                } catch (IOException ae) {
                    LOG.error("Can't access to table " + ae);
                }
            }
        } catch (Exception hae) {
            LOG.error("Hbase is unavailable " + hae);
        }
    }
}

