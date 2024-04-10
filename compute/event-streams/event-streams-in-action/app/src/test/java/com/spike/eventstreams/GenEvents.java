package com.spike.eventstreams;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GenEvents {
    private static final Logger LOG = LoggerFactory.getLogger(GenEvents.class);
    static final String BASE_DIR = "app/src/test/resources";

    public static void main(String[] args) throws IOException {
        final String servers = "localhost:9092";
        KafkaProducer<String, String> producer = new KafkaProducer<>(IProducer.createConfig(servers));

//        System.out.println(System.getProperty("user.dir"));
        sendRawEvents(producer);

        // hold on
        System.in.read();
    }

    private static void send(KafkaProducer<String, String> producer, String topic, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                LOG.error("send failed", exception);
            } else {
                LOG.info("send result: partition {}, offset {}", metadata.partition(), metadata.offset());
            }
        });
    }

    public static void sendRawEvents(KafkaProducer<String, String> producer) throws IOException {
        final String topic = "raw-events";
        List<String> lines = Files.readAllLines(Paths.get(BASE_DIR, "raw-events.json"));
        for (String line : lines) {
            System.out.println(line);
            send(producer, topic, line);
        }
    }
}
