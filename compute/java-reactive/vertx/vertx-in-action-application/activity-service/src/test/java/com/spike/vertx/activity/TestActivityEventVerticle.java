package com.spike.vertx.activity;

import com.google.common.collect.Lists;
import com.spike.vertx.commons.Constants;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.kafka.admin.KafkaAdminClient;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducerRecord;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.rxjava3.sqlclient.SqlClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisplayName("ActivityEventVerticle Tests")
@ExtendWith(VertxExtension.class)
public class TestActivityEventVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(TestActivityEventVerticle.class);

    private KafkaProducer<String, JsonObject> producer;
    private KafkaConsumer<String, JsonObject> consumer;
    private SqlClient sqlClient;

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {
        this.producer = KafkaProducer.create(vertx, KafkaConfig.producer());
        this.consumer = KafkaConsumer.create(vertx, KafkaConfig.consumer(this.getClass().getSimpleName()));
        KafkaAdminClient adminClient = KafkaAdminClient.create(vertx, KafkaConfig.producer());

        this.sqlClient = PgBuilder.client()
                .using(vertx)
                .connectingTo(PgConfig.options())
                .with(PgConfig.poolOptions())
                .build();
        this.sqlClient.preparedQuery("DELETE FROM stepevent")
                .rxExecute()
                .flatMapCompletable(rs -> {
                    return adminClient.rxDeleteTopics(Lists.newArrayList(
                            Constants.Kafka.Topic.incoming_steps,
                            Constants.Kafka.Topic.daily_step_updates));
                })
                .andThen(Completable.fromAction(sqlClient::close)) // ???
                .onErrorComplete()
                .subscribe(
                        testContext::completeNow,
                        testContext::failNow);
    }

    @DisplayName("Verify stats record")
    @Test
    public void test(Vertx vertx, VertxTestContext testContext) {
        vertx.rxDeployVerticle(new ActivityEventVerticle()) // deploy verticle
                .flatMap(id -> {
                    return producer.rxSend(KafkaProducerRecord.create(Constants.Kafka.Topic.incoming_steps,
                            new JsonObject()
                                    .put("deviceId", "123")
                                    .put("deviceSync", 1L)
                                    .put("stepsCount", 120)));
                })
                .flatMap(rm -> {
                    return producer.rxSend(KafkaProducerRecord.create(Constants.Kafka.Topic.incoming_steps,
                            new JsonObject()
                                    .put("deviceId", "123")
                                    .put("deviceSync", 2L)
                                    .put("stepsCount", 130)));
                })
                .subscribe(ok -> LOG.info("Produce done"), testContext::failNow);

        this.consumer.rxSubscribe(Constants.Kafka.Topic.daily_step_updates)
                .doOnComplete(() -> {
                    this.consumer.toObservable()
                            .skip(1) // skip first stats record
                            .subscribe(record -> {
                                        LOG.info("Consumer got record: {}", record);
                                        testContext.verify(() -> {
                                            JsonObject json = record.value();
                                            Assertions.assertThat(json.getString("deviceId")).isEqualTo("123");
                                            Assertions.assertThat(json.containsKey("timestamp")).isTrue();
                                            Assertions.assertThat(json.getInteger("stepsCount")).isEqualTo(250);
                                            testContext.completeNow();
                                        });
                                    },
                                    err -> {
                                        LOG.error("ERROR", err);
                                        testContext.failNow(err);
                                    });
                })
                .subscribe(
                        () -> LOG.info("Subscribe Done"),
                        err -> LOG.error("Subscribe ERROR", err));
    }
}
