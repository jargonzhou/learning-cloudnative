package com.spike.eventstreams;

import com.google.common.collect.Maps;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class GenEventsTest {
    private static final Logger LOG = LoggerFactory.getLogger(GenEventsTest.class);
    public static final String BASE_DIR = "src/test/resources";

    @Test
    public void testGenEvents() throws IOException, InterruptedException {

        Map<String, Object> props = Maps.newHashMap();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 1000);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        CountDownLatch latch = send(producer, "wikipedia-raw", readSamzaRawEvents());

        // hold on
        latch.await();
    }

    public static CountDownLatch send(KafkaProducer<String, String> producer, String topic, List<String> lines) {
        CountDownLatch latch = new CountDownLatch(lines.size());
        for (String line : lines) {
            System.out.println(line);
            send(producer, topic, line, latch);
        }
        return latch;
    }

    private static void send(KafkaProducer<String, String> producer, String topic, String value, CountDownLatch latch) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                LOG.error("send failed", exception);
            } else {
                LOG.info("send result: partition {}, offset {}", metadata.partition(), metadata.offset());
            }
            latch.countDown();
        });
    }

    // samza
    public static List<String> readSamzaRawEvents() throws IOException {
        return Files.readAllLines(Paths.get(BASE_DIR, "wikipedia-raw.json"));
    }
}
