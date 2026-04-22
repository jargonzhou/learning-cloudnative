package com.spike.dataengineering.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.kafka.KafkaContainer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaContainerTest {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaContainerTest.class);

    private static KafkaContainer kafka; // single node

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeAll
    public static void beforeAll() {
        kafka = new KafkaContainer("apache/kafka:3.7.0");
        kafka.start();

        while (!kafka.isRunning()) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        LOG.info("Kafka started.");
    }

    @AfterAll
    public static void afterAll() {
        kafka.stop();
    }

    @Test
    public void createTopic() throws ExecutionException, InterruptedException {
        String topicName = "test-topic";
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 120 * 1000); // default: 120s
        try (AdminClient adminClient = AdminClient.create(props)) {
            CreateTopicsResult newTopic = adminClient.createTopics(List.of(
                    new NewTopic(topicName, 3, (short) 1))); // replication factor must be 1 in single node
            LOG.info("Create topic result: partition={}", newTopic.numPartitions(topicName).get());
            Assertions.assertThat(newTopic).isNotNull();
        }
    }

    @Test
    public void produceThenConsume() {
        System.err.println(kafka.getBootstrapServers());

        // producer
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", kafka.getBootstrapServers());
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        final String topic = "example-topic";
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties)) {
            String value = "hello";
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value); // no key
            RecordMetadata sendResult = producer.send(record).get();
            System.err.println(sendResult + ": " + value);
            Assertions.assertThat(sendResult).isNotNull();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // consumer
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", kafka.getBootstrapServers());
        consumerProperties.put("group.id", "c-group");
        consumerProperties.put("auto.offset.reset", "earliest");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties)) {
            consumer.subscribe(Collections.singletonList(topic));

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            for (ConsumerRecord<String, String> record : records) {
                System.err.println(record);
            }
        }

    }

}
