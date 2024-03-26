package com.spike.vertx.ingestion;

import com.spike.vertx.commons.Constants;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfig {
    public static Map<String, String> producer() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", Constants.IngestionService.KAFKA_SERVERS);
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        config.put("acks", "1");
        return config;
    }
}
