/***********************************************************************************
 *
 * Copyright 2014 Yota Lab LLC, Russia
 * Copyright 2014 Seconca Holdings Limited, Cyprus
 *
 *  This source code is Yota Lab Confidential Proprietary
 *  This software is protected by copyright.  All rights and titles are reserved.
 *  You shall not use, copy, distribute, modify, decompile, disassemble or reverse
 *  engineer the software. Otherwise this violation would be treated by law and
 *  would be subject to legal prosecution.  Legal use of the software provides
 *  receipt of a license from the right holder only.
 *
 *
 ************************************************************************************/

package tv.okko.devops.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.log4j.Logger;

/**
 * HBase compaction.
 */
public class HBaseCompaction {
    private static final Logger LOG = Logger.getLogger(HBaseCompaction.class.getName());

    /**
     * Main.
     * @param args args
     * @throws IOException on errors
     */
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

