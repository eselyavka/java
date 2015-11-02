package com.example.kafka;

import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.*;

public class SimpleHLKafkaConsumer {
    private final ConsumerConnector consumer;
    private final String topic_id;
    private final Integer partNum;
    private ExecutorService executor;
    private static final Logger LOG = Logger.getLogger(SimpleKafkaProducer.class.getName());

    public SimpleHLKafkaConsumer(String topic_id) throws Exception {
        this.partNum = getPartNum(topic_id);
        this.consumer = Consumer.createJavaConsumerConnector(createConsumerConfig());
        this.topic_id = topic_id;
    }

    private Integer getPartNum(String topic_id) throws Exception {
        KafkaZKClient zk = new KafkaZKClient(createConsumerConfig().props().getString("zookeeper.connect"));
        Integer num = 1;
        zk.startZK();
        num = zk.getElement("/brokers/topics/" + topic_id + "/partitions").size();
        zk.closeZK();
        return num;
    }

    private static ConsumerConfig createConsumerConfig() {
        Properties prop = new Properties();
        prop.put("zookeeper.connect", "localhost:2181");
        prop.put("group.id", "group1");
        prop.put("zookeeper.session.timeout.ms", "500");
        prop.put("zookeeper.sync.time.ms", "250");
        prop.put("auto.commit.interval.ms", "1000");
        prop.put("consumer.timeout.ms", "60000");
        prop.put("autooffset.reset", "largest");

        return new ConsumerConfig(prop);
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        try {
            if (executor != null) {
                executor.shutdown();
                if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                    if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                        LOG.debug("Timed out waiting for consumer threads to shut down, exiting uncleanly");
                    }
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            LOG.debug("Interrupted during shutdown, exiting uncleanly");
            Thread.currentThread().interrupt();
        }
    }

    private void consumeMessage() throws ConsumerTimeoutException, UnsupportedEncodingException {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic_id, partNum);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streamList = consumerStreamMap.get(topic_id);
        executor = Executors.newFixedThreadPool(partNum);

        int count = 0;
        for (final KafkaStream<byte[], byte[]> stream : streamList) {
            final int threadCount = count;
            executor.submit(new Runnable() {
                public void run() {
                    ConsumerIterator<byte[], byte[]> consumerIterator = stream.iterator();
                    while (consumerIterator.hasNext()) {
                        try {
                            LOG.info("Message: '" + new String(consumerIterator.next().message(), "UTF-8") + "' from thread number: " + threadCount);
                        } catch (UnsupportedEncodingException e) {
                            LOG.error(ExceptionUtils.getStackTrace(e));
                        }
                    }
                }
            });
            count++;
        }
    }

    public static void main(String[] args) {
        Integer argc = args.length;

        if (argc == 0) {
            throw new IllegalArgumentException("Usage: " + SimpleHLKafkaConsumer.class.getName() + " <topic name>");
        }

        String topic = args[0];

        SimpleHLKafkaConsumer sc = null;

        try {
            sc = new SimpleHLKafkaConsumer(topic);
            sc.consumeMessage();
            sc.executor.awaitTermination(15000, TimeUnit.MILLISECONDS);
        } catch (ConsumerTimeoutException e) {
            if (sc != null) {
                sc.shutdown();
            }
            LOG.info("Timeout is reached");
        } catch (InterruptedException ie){
            LOG.error(ExceptionUtils.getStackTrace(ie));
        } catch (Exception e) {
            LOG.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (sc != null) {
                sc.shutdown();
            }
        }
    }
}
