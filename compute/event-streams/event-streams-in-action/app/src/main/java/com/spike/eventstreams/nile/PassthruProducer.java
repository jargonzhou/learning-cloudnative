package com.spike.eventstreams.nile;

import org.apache.kafka.clients.producer.KafkaProducer;

public class PassthruProducer implements IProducer {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public PassthruProducer(String servers, String topic) {
        this.producer = new KafkaProducer<>(IProducer.createConfig(servers));
        this.topic = topic;
    }

    @Override
    public void process(String value) {
        IProducer.write(this.producer, this.topic, value);
    }
}
