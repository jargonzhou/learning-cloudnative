package com.spike.eventstreams;

import com.spike.eventstreams.nile.IProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.spike.eventstreams.AppTest.servers;

public class GenEventsTest {
    private static final Logger LOG = LoggerFactory.getLogger(GenEventsTest.class);

    @BeforeAll
    public static void setUpBeforeAll() {
        // on Windows
        // Suppressed: java.nio.file.AccessDeniedException: /tmp/kraft-combined-logs/bad-events-0 ->
        //  /tmp/kraft-combined-logs/bad-events-0.eb2d7fa3f8f44ad79ff35b45a1f3f772-stray
//        try (AdminClient client = AppTest.newAdminClient()) {
//            DeleteTopicsResult result = client.deleteTopics(ImmutableList.of(
//                    App.Topics.RAW_EVENTS,
//                    App.Topics.BAD_EVENTS,
//                    App.Topics.ENRICHED_EVENTS));
//            result.all().toCompletionStage().whenComplete((v, e) -> {
//                if (e == null) {
//                    LOG.info("Delete topics succeed");
//                } else {
//                    LOG.error("Delete topics failed", e);
//                }
//            });
//        }
    }

    @Test
    public void testGenEvents() throws IOException, InterruptedException {

        KafkaProducer<String, String> producer = new KafkaProducer<>(IProducer.createConfig(servers));
// example: Nile
        CountDownLatch latch = send(producer, "raw-events", readRawEvents());

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

    public static List<String> readRawEvents() throws IOException {
        return Files.readAllLines(Paths.get(AppTest.BASE_DIR, "raw-events.json"));
    }
}
