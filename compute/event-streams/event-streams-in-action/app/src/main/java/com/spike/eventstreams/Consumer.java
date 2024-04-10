package com.spike.eventstreams;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Map;

public class Consumer {
    private final KafkaConsumer<String, String> consumer;
    private final String topic;

    public Consumer(String servers, String groupId, String topic) {
        this.consumer = new KafkaConsumer<>(this.createConfig(servers, groupId));
        this.topic = topic;
    }

    public void run(IProducer producer) {
        this.consumer.subscribe(Lists.newArrayList(this.topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                producer.process(record.value());
            }
        }
    }

    private Map<String, Object> createConfig(String servers, String groupId) {
        Map<String, Object> props = Maps.newHashMap();
        props.put("bootstrap.servers", servers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}
