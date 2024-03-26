package com.spike.vertx.congrats;

import com.google.common.collect.Lists;
import com.spike.vertx.commons.Constants;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.mail.MailClient;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;
import io.vertx.rxjava3.kafka.admin.KafkaAdminClient;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class TestCongratsVerticle {


    private KafkaProducer<String, JsonObject> producer;
    private WebClient webClient;

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {
        Map<String, String> conf = new HashMap<>();
        conf.put("bootstrap.servers", "localhost:9092");
        conf.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        conf.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        conf.put("acks", "1");
        producer = KafkaProducer.create(vertx, conf);
        webClient = WebClient.create(vertx);
        KafkaAdminClient adminClient = KafkaAdminClient.create(vertx, conf);
        adminClient
                .rxDeleteTopics(Lists.newArrayList(
                        Constants.Kafka.Topic.incoming_steps,
                        Constants.Kafka.Topic.daily_step_updates))
                .onErrorComplete()
                .andThen(vertx.rxDeployVerticle(new CongratsVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new FakeUserService()))
                .ignoreElement()
                .andThen(webClient.delete(Constants.CongratsService.MAIL_HTTP_PORT, Constants.CongratsService.MAIL_HOSTNAME, "/api/v1/messages").rxSend())
                .ignoreElement()
                .delay(1, TimeUnit.SECONDS, RxHelper.scheduler(vertx))
                .subscribe(testContext::completeNow, testContext::failNow);
    }

    private KafkaProducerRecord<String, JsonObject> record(String deviceId, long steps) {
        LocalDateTime now = LocalDateTime.now();
        String key = deviceId + ":" + now.getYear() + "-" + now.getMonth() + "-" + now.getDayOfMonth();
        JsonObject json = new JsonObject()
                .put("deviceId", deviceId)
                .put("timestamp", now.toString())
                .put("stepsCount", steps);
        return KafkaProducerRecord.create("daily.step.updates", key, json);
    }

    @Test
    @DisplayName("Smoke test to send a mail using mailhog")
    void smokeTestSendmail(Vertx vertx, VertxTestContext testContext) {
        MailConfig config = new MailConfig()
                .setHostname("localhost")
                .setPort(1025);
        MailClient client = MailClient.createShared(vertx, config);
        MailMessage message = new MailMessage()
                .setFrom("a@b.tld")
                .setSubject("Yo")
                .setTo("c@d.tld")
                .setText("This is cool");
        client
                .rxSendMail(message)
                .subscribe(
                        ok -> testContext.completeNow(),
                        testContext::failNow);
    }

    @Test
    @DisplayName("No email must be sent below 10k steps")
    void checkNothingBelow10k(Vertx vertx, VertxTestContext testContext) {
        producer
                .rxSend(record("123", 5000))
                .ignoreElement()
                .delay(3, TimeUnit.SECONDS, RxHelper.scheduler(vertx))
                .andThen(webClient
                        .get(Constants.CongratsService.MAIL_HTTP_PORT, Constants.CongratsService.MAIL_HOSTNAME, "/api/v2/search?kind=to&query=foo@mail.tld")
                        .as(BodyCodec.jsonObject()).rxSend())
                .map(HttpResponse::body)
                .subscribe(
                        json -> {
                            testContext.verify(() -> Assertions.assertThat(json.getInteger("total")).isEqualTo(0));
                            testContext.completeNow();
                        },
                        testContext::failNow);
    }

    @Test
    @DisplayName("An email must be sent for 10k+ steps")
    void checkSendsOver10k(Vertx vertx, VertxTestContext testContext) {
        producer
                .rxSend(record("123", 11_000))
                .ignoreElement()
                .delay(3, TimeUnit.SECONDS, RxHelper.scheduler(vertx))
                .andThen(webClient
                        .get(Constants.CongratsService.MAIL_HTTP_PORT, Constants.CongratsService.MAIL_HOSTNAME, "/api/v2/search?kind=to&query=foo@mail.tld")
                        .as(BodyCodec.jsonObject()).rxSend())
                .map(HttpResponse::body)
                .subscribe(
                        json -> {
                            testContext.verify(() -> Assertions.assertThat(json.getInteger("total")).isEqualTo(1));
                            testContext.completeNow();
                        },
                        testContext::failNow);
    }

    @Test
    @DisplayName("Just one email must be sent to a user for 10k+ steps on single day")
    void checkNotTwiceToday(Vertx vertx, VertxTestContext testContext) {
        producer
                .rxSend(record("123", 11_000))
                .ignoreElement()
                .delay(3, TimeUnit.SECONDS, RxHelper.scheduler(vertx))
                .andThen(webClient
                        .get(Constants.CongratsService.MAIL_HTTP_PORT, Constants.CongratsService.MAIL_HOSTNAME, "/api/v2/search?kind=to&query=foo@mail.tld")
                        .as(BodyCodec.jsonObject()).rxSend())
                .map(HttpResponse::body)
                .map(json -> {
                    testContext.verify(() -> Assertions.assertThat(json.getInteger("total")).isEqualTo(1));
                    return json;
                })
                .ignoreElement()
                .andThen(producer.rxSend(record("123", 11_100)))
                .ignoreElement()
                .delay(3, TimeUnit.SECONDS, RxHelper.scheduler(vertx))
                .andThen(webClient
                        .get(Constants.CongratsService.MAIL_HTTP_PORT, Constants.CongratsService.MAIL_HOSTNAME, "/api/v2/search?kind=to&query=foo@mail.tld")
                        .as(BodyCodec.jsonObject()).rxSend())
                .map(HttpResponse::body)
                .subscribe(
                        json -> {
                            testContext.verify(() -> Assertions.assertThat(json.getInteger("total")).isEqualTo(1));
                            testContext.completeNow();
                        },
                        testContext::failNow);
    }

    public static class FakeUserService extends AbstractVerticle {

        private static final Logger logger = LoggerFactory.getLogger(FakeUserService.class);

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
                    .put("email", "foo@mail.tld");
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(notAllData.encode());
        }

        private void owns(RoutingContext ctx) {
            logger.info("Device ownership request {}", ctx.request().path());
            JsonObject notAllData = new JsonObject()
                    .put("username", "Foo");
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(notAllData.encode());
        }
    }

}
