package tv.okko.devops.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Get;
import java.nio.charset.Charset;
import org.apache.log4j.Logger;


public class HBaseManager {
    private static final Logger LOG = Logger.getLogger(HBaseManager.class.getName());

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        //HConnection connection = HConnectionManager.createConnection(conf);
        HTablePool pool = new HTablePool(conf, 2);

        try {
            for (;;) {            
            //String tableName = "tbl_temp";
            //byte[] bytes = tableName.getBytes(Charset.forName("UTF-8"));
            //HTable table = new HTable(bytes,connection);
            //HTable table = new HTable(conf,bytes);
            Put put = new Put(Bytes.toBytes("6"));
            put.add(Bytes.toBytes("cf1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"));
            put.add(Bytes.toBytes("cf2"), Bytes.toBytes("qual2"), Bytes.toBytes("val2"));
            
            HTableInterface table1 = pool.getTable("tbl_temp");
            table1.put(put);
            
            HTableInterface table2 = pool.getTable("tbl_temp");
            table2.put(put);
            
            HTableInterface table3 = pool.getTable("tbl_temp");
            table3.put(put);

            table1.close();
            table2.close();
            table3.close();
            /*try {
                table.put(put);
            } finally {
                table.close();
                //pool.close();
            }*/
            }
        } catch (Exception hae) {
            LOG.error("Hbase is unavailable " + hae);
        }
    }
}

