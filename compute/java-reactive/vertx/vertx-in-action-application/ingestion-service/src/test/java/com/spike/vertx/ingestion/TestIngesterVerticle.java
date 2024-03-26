package com.spike.vertx.ingestion;

import com.google.common.collect.Lists;
import com.spike.vertx.commons.Constants;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.amqp.AmqpClient;
import io.vertx.rxjava3.amqp.AmqpMessage;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.kafka.admin.KafkaAdminClient;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@DisplayName("IngesterVerticle Tests")
@ExtendWith(VertxExtension.class)
public class TestIngesterVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(TestIngesterVerticle.class);

    private static RequestSpecification requestSpecification;

    @BeforeAll
    public static void setUpBeforeAll() {
        requestSpecification = new RequestSpecBuilder()
                .addFilters(Arrays.asList(new ResponseLoggingFilter(), new RequestLoggingFilter()))
                .setBaseUri("http://localhost:" + Constants.IngestionService.HTTP_PORT)
                .build();
    }

    private static Map<String, String> consumer(String group) {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", Constants.IngestionService.KAFKA_SERVERS);
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");
        config.put("group.id", group);
        return config;
    }

    private AmqpClient amqpClient;
    private KafkaConsumer<String, JsonObject> kafkaConsumer;

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {
        this.kafkaConsumer = KafkaConsumer.create(vertx,
                consumer("ingester-test-" + System.currentTimeMillis()));
        this.amqpClient = AmqpClient.create(vertx, AMQPConfig.clientOptions());

        KafkaAdminClient adminClient = KafkaAdminClient.create(vertx,
                consumer("ingester-test-" + System.currentTimeMillis()));
        vertx.rxDeployVerticle(new IngesterVerticle()) // deploy verticle
                .delay(500, TimeUnit.MILLISECONDS, RxHelper.scheduler(vertx))
                .flatMapCompletable(id -> adminClient
                        .rxDeleteTopics(Lists.newArrayList(Constants.Kafka.Topic.incoming_steps)))
                .onErrorComplete()
                .subscribe(testContext::completeNow, testContext::failNow);
    }

    @DisplayName("Ingest an AMQP message")
    @Test
    public void amqpIngest(Vertx vertx, VertxTestContext testContext) {
        JsonObject payload = new JsonObject()
                .put("deviceId", "123")
                .put("deviceSync", 1L)
                .put("stepsCount", 500);

        this.amqpClient.rxConnect()
                .flatMap(conn -> conn.rxCreateSender(Constants.AMQP.Address.step_events))
                .subscribe(
                        sender -> {
                            AmqpMessage msg = AmqpMessage.create()
                                    .durable(true)
                                    .ttl(5000L)
                                    .withJsonObjectAsBody(payload)
                                    .build();
                            sender.send(msg);
                        },
                        testContext::failNow);


        this.kafkaConsumer.subscribe(Constants.Kafka.Topic.incoming_steps)
                .doOnComplete(() -> {
                    kafkaConsumer.toObservable()
                            .subscribe(
                                    record -> testContext.verify(() -> {
                                        Assertions.assertThat(record.key()).isEqualTo("123");
                                        JsonObject json = record.value();
                                        Assertions.assertThat(json.getString("deviceId")).isEqualTo("123");
                                        Assertions.assertThat(json.getLong("deviceSync")).isEqualTo(1L);
                                        Assertions.assertThat(json.getInteger("stepsCount")).isEqualTo(500);
                                        testContext.completeNow();
                                    }),
                                    testContext::failNow);
                })
                .subscribe(
                        () -> LOG.info("Subscribe Done"),
                        err -> LOG.error("Subscribe ERROR", err));
    }

    @DisplayName("Ingest a HTTP message")
    @Test
    public void httpIngest(Vertx vertx, VertxTestContext testContext) {
        JsonObject payload = new JsonObject()
                .put("deviceId", "456")
                .put("deviceSync", 3L)
                .put("stepsCount", 125);
        RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(payload.encode())
                .post(Constants.IngestionService.HTTPApi.INGEST)
                .then()
                .assertThat()
                .statusCode(200);

        // TODO(zhoujiagen) how to write the rxjava version consumer???
//        kafkaConsumer.handler(record -> {
//            LOG.info("{} {} {}: {}", record.topic(), record.partition(), record.offset(), record.value());
//        });
        kafkaConsumer.subscribe(Constants.Kafka.Topic.incoming_steps)
                .doOnComplete(
                        () -> {
                            kafkaConsumer.toObservable()
                                    .subscribe(
                                            record -> {
                                                LOG.info("Consumer got record {} {} {}: {}", record.topic(), record.partition(), record.offset(), record.value());
                                                testContext.verify(() -> {
                                                    Assertions.assertThat(record.key()).isEqualTo("456");
                                                    JsonObject json = record.value();
                                                    Assertions.assertThat(json.getString("deviceId")).isEqualTo("456");
                                                    Assertions.assertThat(json.getLong("deviceSync")).isEqualTo(3L);
                                                    Assertions.assertThat(json.getInteger("stepsCount")).isEqualTo(125);
                                                    testContext.completeNow();
                                                });
                                            },
                                            testContext::failNow);
                        })
                .subscribe(
                        () -> LOG.info("Subscribe Done"),
                        err -> LOG.error("Subscribe ERROR", err));
    }
}
