package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import java.util.Properties;

public class SimpleKafkaProducer {
    private static KafkaProducer<String, String> producer;
    private static final Logger LOG = Logger.getLogger(SimpleKafkaProducer.class.getName());

    public SimpleKafkaProducer() {
        Properties prop = new Properties();
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("bootstrap.servers", "localhost:9092");
        prop.put("acks", "1");
        producer = new KafkaProducer<String, String>(prop);

    }

    private void publishMessage(String topic, String message) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, message);
        LOG.info("Sending message: '" + message + "' to topic: '" + topic + "'");
        producer.send(producerRecord);
        producer.close();
    }

    public static void main(String[] args) {
        Integer argc = args.length;

        if (argc == 0 || argc == 1) {
            throw new IllegalArgumentException("Usage: " + SimpleKafkaProducer.class.getName() + " <topic name> <message>");
        }

        String topic = args[0];
        String message = args[1];

        SimpleKafkaProducer simpleKafkaProducer = new SimpleKafkaProducer();
        simpleKafkaProducer.publishMessage(topic, message);
    }
}
