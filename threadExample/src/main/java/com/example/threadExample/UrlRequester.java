package com.example.threadExample;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by eseliavka on 07.08.15.
 */
public class UrlRequester implements Runnable {
    private Thread t;
    private String threadName;
    private Counter counter;
    private long bulkSize;
    private long mobBulkSize;
    private URLList urls;

    UrlRequester(String name, Counter counter, Integer bulkSize, Integer mobBulkSize, URLList urls) {
        this.threadName = name;
        this.counter = counter;
        this.urls = urls;
        this.bulkSize = bulkSize;
        this.mobBulkSize = mobBulkSize;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        System.out.println("Running: " + threadName);
        long prev = counter.count;
        if (counter.count == (bulkSize + mobBulkSize)) {
            counter.decrement(bulkSize + mobBulkSize);
        } else {
            counter.decrement(bulkSize);
        }
        for (long i = counter.count; i < prev; i++) {
            System.out.println("Trying get ip for:" + urls.getUrllist().get((int) i) + " from thread: " + threadName);
            try {
                InetAddress inetAddress = InetAddress.getByName(urls.getUrllist().get((int) i));
                System.out.println("Thread: " + threadName + " - Hostname:" + inetAddress.getHostName() + " - Ip:" + inetAddress.getHostAddress());
            } catch (UnknownHostException e) {
                continue;
            }
        }
        System.out.println("Stopping: " + threadName);
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
