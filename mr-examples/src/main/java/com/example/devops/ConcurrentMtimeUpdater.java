package com.example.devops;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class ConcurrentMtimeUpdater implements Runnable {
    private Thread t;
    private String threadName;
    private FileSystem dfs;
    private Path p;
    private Long epoch;
    private static final Log LOG = LogFactory.getLog(ConcurrentMtimeUpdater.class);

    public ConcurrentMtimeUpdater(Configuration conf, String name, String fileName, String e) throws IOException {
        threadName = name;
        dfs = FileSystem.get(conf);
        p = new Path(fileName);
        try {
            epoch = Long.parseLong(e);
        } catch (NumberFormatException nfe) {
            LOG.error(nfe.getMessage());
            epoch = System.currentTimeMillis();
        }
    }

    public void run() {
        LOG.info("Running " + threadName);
        try {
            if (dfs.createNewFile(p)) {
                LOG.info("Thread " + threadName + " create " + p.getName() + " file on hdfs");
            }
            dfs.setTimes(p, epoch, -1);
        } catch (IOException ioe) {
            LOG.error("Thread " + threadName + " can't perform hdfs operation.");
        }
    }

    public void start() {
        LOG.info("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
