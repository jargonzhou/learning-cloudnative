package com.spike.vertx.eventstats;

import com.spike.vertx.commons.Constants;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfig {
    public static Map<String, String> producer() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", Constants.EventStatsService.KAFKA_SERVERS);
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        config.put("acks", "1");
        return config;
    }

    public static Map<String, String> consumer(String group) {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", Constants.EventStatsService.KAFKA_SERVERS);
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "true"); // ???
        config.put("group.id", group);
        return config;
    }
}
