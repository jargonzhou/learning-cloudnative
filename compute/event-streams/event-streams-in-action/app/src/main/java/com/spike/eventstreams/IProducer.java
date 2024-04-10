package com.spike.eventstreams;

import com.google.common.collect.Maps;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public interface IProducer {
    Logger LOG = LoggerFactory.getLogger(IProducer.class);

    void process(String value);

    static void write(KafkaProducer<String, String> producer, String topic, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                LOG.error("send failed", exception);
            } else {
                LOG.info("send result: partition {}, offset {}", metadata.partition(), metadata.offset());
            }
        });
    }

    static Map<String, Object> createConfig(String servers) {
        Map<String, Object> props = Maps.newHashMap();
        props.put("bootstrap.servers", servers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 1000);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}
