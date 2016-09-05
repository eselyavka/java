package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import java.io.IOException;

public class ConcurrentMtimeUpdaterExecutor {
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        ConcurrentMtimeUpdater t = new ConcurrentMtimeUpdater(conf, "thread1", args[0], args[1]);
        ConcurrentMtimeUpdater t2 = new ConcurrentMtimeUpdater(conf, "thread2", args[0], args[1]);
        t.start();
        t2.start();
    }
}
