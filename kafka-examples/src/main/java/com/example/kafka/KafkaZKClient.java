package com.example.kafka;

/**
 * Created by eseliavka on 26.10.15.
 */

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class KafkaZKClient implements Watcher {
    ZooKeeper zk = null;
    String hostPort;
    Stat stat;

    KafkaZKClient(String hostPort) {
        this.hostPort = hostPort;
        this.stat = new Stat();
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

    void createElement(String node, byte[] data) throws Exception {
        zk.create(node, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    List getElement(String node) throws Exception {
        return zk.getChildren(node, this);
    }

    public void process(WatchedEvent e) {
    }
}
