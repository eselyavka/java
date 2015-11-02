package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.UUID;

public class SimpleKafkaProducer {
    private static KafkaProducer<String, String> producer;
    private final String topic;
    private final String message;
    private final Integer msgcount;
    private static final Logger LOG = Logger.getLogger(SimpleKafkaProducer.class.getName());

    public SimpleKafkaProducer(String topic, String message, Integer msgcount) {
        this.topic = topic;
        this.message = message;
        this.msgcount = msgcount;
        producer = new KafkaProducer<String, String>(createProducerConfig());

    }

    private static Properties createProducerConfig() {
        Properties prop = new Properties();
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("bootstrap.servers", "localhost:9092");
        prop.put("acks", "1");
        return prop;
    }

    private void publishMessage() {
        ProducerRecord<String, String> producerRecord = null;
        Integer partNum = producer.partitionsFor(topic).size();
        UUID key;

        for (int i = 0; i < this.msgcount; i++) {
            if (partNum > 1) {
                key = UUID.randomUUID();
                LOG.debug("Topic: '" + topic + "' consist from " + partNum + " partitions");
                LOG.info("Sending message: '" + message + "' to topic: '" + topic + "' with key: " + key);
                producerRecord = new ProducerRecord<String, String>(topic, key.toString(), message);
                producer.send(producerRecord);
            } else {
                LOG.debug("Topic: '" + topic + "' consist from " + partNum + " partition");
                LOG.info("Sending message: '" + message + "' to topic: '" + topic);
                producerRecord = new ProducerRecord<String, String>(topic, message);
                producer.send(producerRecord);
            }
        }
        producer.close();
    }

    public static void main(String[] args) {
        Integer argc = args.length;

        if (argc == 0 || argc < 2) {
            throw new IllegalArgumentException("Usage: " + SimpleKafkaProducer.class.getName() + " <topic name> <message> [number of message, default 100]");
        }

        String topic = args[0];
        String message = args[1];
        Integer msgcount = 100;

        try {
            msgcount = Integer.parseInt(args[2]);
        } catch (NumberFormatException nfe) {
            LOG.warn("Argument '" + args[2] + "' is not a number, use default value:" + msgcount);
        } catch (ArrayIndexOutOfBoundsException ae) {
            LOG.warn("Message count is not set, use default value:" + msgcount);
        }

        SimpleKafkaProducer simpleKafkaProducer = new SimpleKafkaProducer(topic, message, msgcount);
        simpleKafkaProducer.publishMessage();
    }
}
