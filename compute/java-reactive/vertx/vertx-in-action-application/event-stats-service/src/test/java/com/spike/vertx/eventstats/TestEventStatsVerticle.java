package com.spike.vertx.eventstats;

import com.google.common.collect.Lists;
import com.spike.vertx.commons.Constants;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.kafka.admin.KafkaAdminClient;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducerRecord;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@DisplayName("EventStatsVerticle Tests")
@ExtendWith(VertxExtension.class)
public class TestEventStatsVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(TestEventStatsVerticle.class);

    private KafkaProducer<String, JsonObject> producer;
    private KafkaConsumer<String, JsonObject> consumer;

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {
        this.producer = KafkaProducer.create(vertx, KafkaConfig.producer());
        this.consumer = KafkaConsumer.create(vertx, KafkaConfig.consumer(UUID.randomUUID().toString()));
        KafkaAdminClient adminClient = KafkaAdminClient.create(vertx, KafkaConfig.producer());
        adminClient
                .rxDeleteTopics(Lists.newArrayList(
                        Constants.Kafka.Topic.incoming_steps,
                        Constants.Kafka.Topic.daily_step_updates,
                        Constants.Kafka.Topic.event_stats_throughput,
                        Constants.Kafka.Topic.event_stats_city_trends_update))
                .onErrorComplete()
                .andThen(vertx.rxDeployVerticle(new EventStatsVerticle())) // deploy verticle
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new FakeUserService()))
                .ignoreElement()
                .subscribe(
                        testContext::completeNow,
                        testContext::failNow);
    }

    private KafkaProducerRecord<String, JsonObject> dailyStepsUpdateRecord(String deviceId, long steps) {
        LocalDateTime now = LocalDateTime.now();
        String key = deviceId + ":" + now.getYear() + "-" + now.getMonth() + "-" + now.getDayOfMonth();
        JsonObject json = new JsonObject()
                .put("deviceId", deviceId)
                .put("timestamp", now.toString())
                .put("stepsCount", steps);
        return KafkaProducerRecord.create(Constants.Kafka.Topic.daily_step_updates, key, json);
    }

    private KafkaProducerRecord<String, JsonObject> incomingStepsRecord(String deviceId, long syncId, long steps) {
        LocalDateTime now = LocalDateTime.now();
        String key = deviceId + ":" + now.getYear() + "-" + now.getMonth() + "-" + now.getDayOfMonth();
        JsonObject json = new JsonObject()
                .put("deviceId", deviceId)
                .put("syncId", syncId)
                .put("stepsCount", steps);
        return KafkaProducerRecord.create(Constants.Kafka.Topic.incoming_steps, key, json);
    }

    @Test
    @DisplayName("Incoming activity throughput computation")
    public void throughput(VertxTestContext testContext) {
        for (int i = 0; i < 10; i++) {
            producer.send(this.incomingStepsRecord("abc", (long) i, 10))
                    .toObservable()
                    .subscribe();
        }
        consumer.subscribe(Constants.Kafka.Topic.event_stats_throughput)
                .subscribe(
                        () -> consumer.toObservable()
                                .subscribe(record -> testContext.verify(() -> {
                                    LOG.info("Consumer got record: {}", record);
                                    JsonObject data = record.value();
                                    Assertions.assertThat(data.getInteger("seconds")).isEqualTo(5);
                                    Assertions.assertThat(data.getInteger("count")).isEqualTo(10);
                                    Assertions.assertThat(data.getDouble("throughput")).isCloseTo(2.0d,
                                            Offset.offset(0.01d));
                                    testContext.completeNow();
                                }), testContext::failNow)
                );
    }

    @Test
    @DisplayName("User activity updates")
    public void userActivityUpdate(VertxTestContext testContext) {
        producer.send(this.dailyStepsUpdateRecord("abc", 2500))
                .toObservable()
                .subscribe();

        consumer.subscribe(Constants.Kafka.Topic.event_stats_user_activity_updates)
                .subscribe(
                        () -> consumer.toObservable()
                                .subscribe(
                                        record -> testContext.verify(() -> {
                                            JsonObject data = record.value();
                                            Assertions.assertThat(data.getString("deviceId")).isEqualTo("abc");
                                            Assertions.assertThat(data.getString("username")).isEqualTo("Foo");
                                            Assertions.assertThat(data.getInteger("stepsCount")).isEqualTo(2500);
                                            Assertions.assertThat(data.containsKey("timestamp")).isTrue();
                                            Assertions.assertThat(data.containsKey("city")).isTrue();
                                            Assertions.assertThat(data.containsKey("makePublic")).isTrue();
                                            testContext.completeNow();
                                        }), testContext::failNow));

    }

    @Test
    @DisplayName("City trend updates")
    public void cityTrendUpdate(VertxTestContext testContext) {
        producer.send(this.dailyStepsUpdateRecord("abc", 2500))
                .toObservable()
                .subscribe();
        producer.send(this.dailyStepsUpdateRecord("abc", 2500))
                .toObservable()
                .subscribe();

        consumer.subscribe(Constants.Kafka.Topic.event_stats_city_trends_update)
                .subscribe(
                        () -> consumer.toObservable()
                                .subscribe(record -> testContext.verify(() -> {
                                    JsonObject data = record.value();
                                    Assertions.assertThat(data.getInteger("seconds")).isEqualTo(5);
                                    Assertions.assertThat(data.getInteger("updates")).isEqualTo(2);
                                    Assertions.assertThat(data.getLong("stepsCount")).isEqualTo(5000L);
                                    Assertions.assertThat(data.getString("city")).isEqualTo("Lyon");
                                    testContext.completeNow();
                                }), testContext::failNow));
    }


    public static class FakeUserService extends AbstractVerticle {

        private static final Logger logger = LoggerFactory.getLogger(FakeUserService.class);

        private String deviceId;

        @Override
        public Completable rxStart() {
            Router router = Router.router(vertx);
            router.get("/owns/:deviceId").handler(this::owns);
            router.get("/:username").handler(this::username);
            return vertx.createHttpServer()
                    .requestHandler(router)
                    .rxListen(Constants.UserProfileService.HTTP_PORT)
                    .ignoreElement();
        }

        private void username(RoutingContext ctx) {
            logger.info("User data request {}", ctx.request().path());
            JsonObject notAllData = new JsonObject()
                    .put("username", "Foo")
                    .put("email", "foo@mail.tld")
                    .put("deviceId", deviceId)
                    .put("city", "Lyon")
                    .put("makePublic", true);
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(notAllData.encode());
        }

        private void owns(RoutingContext ctx) {
            logger.info("Device ownership request {}", ctx.request().path());
            deviceId = ctx.pathParam("deviceId");
            JsonObject notAllData = new JsonObject()
                    .put("username", "Foo")
                    .put("deviceId", deviceId);
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(notAllData.encode());
        }
    }
}
