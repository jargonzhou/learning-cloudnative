package com.spike.vertx.congrats;

import com.spike.vertx.commons.Constants;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.ext.mail.MailClient;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumer;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CongratsVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(CongratsVerticle.class);

    private WebClient webClient;
    private MailClient mailClient;

    @Override
    public Completable rxStart() {
        webClient = WebClient.create(vertx);
        mailClient = MailClient.create(vertx, MailerConfig.config());

        KafkaConsumer<String, JsonObject> consumer = KafkaConsumer.create(vertx,
                KafkaConfig.consumer(Constants.CongratsService.KAFKA_CONSUMER_GROUP_ID));
        consumer.subscribe(Constants.Kafka.Topic.daily_step_updates)
                .subscribe(
                        () -> consumer.toObservable()
                                .filter(this::above10k)
                                .distinct(KafkaConsumerRecord::key)
                                .flatMapSingle(this::sendEmail)
                                .doOnError(err -> LOG.error("ERROR", err))
                                .retryWhen(this::retryLater)
                                .subscribe(mailResult -> LOG.info("Congratulated {}", mailResult.getRecipients())));

        return Completable.complete();
    }

    private Single<MailResult> sendEmail(KafkaConsumerRecord<String, JsonObject> record) {
        String deviceId = record.value().getString("deviceId");
        Integer stepsCount = record.value().getInteger("stepsCount");
        return webClient.get(Constants.UserProfileService.HTTP_PORT,
                        Constants.UserProfileService.HOST,
                        String.format(Constants.UserProfileService.HTTPApi.OWNS_DEVICE_ID, deviceId))
                .as(BodyCodec.jsonObject())
                .rxSend()
                .map(HttpResponse::body)
                .map(json -> json.getString("username"))
                .flatMap(this::getUserEmail)
                .map(email -> makeEmail(stepsCount, email))
                .flatMap(mailClient::rxSendMail);
    }

    private Single<String> getUserEmail(String username) {
        return webClient.get(Constants.UserProfileService.HTTP_PORT,
                        Constants.UserProfileService.HOST,
                        String.format(Constants.UserProfileService.HTTPApi.USERNAME, username))
                .as(BodyCodec.jsonObject())
                .rxSend()
                .map(HttpResponse::body)
                .map(json -> json.getString("email"));
    }

    private MailMessage makeEmail(Integer stepsCount, String email) {
        return new MailMessage()
                .setFrom("noreply@example.com")
                .setTo(email)
                .setSubject("Congratulations!")
                .setText("Congratulations on reaching " + stepsCount + " steps today!\n\n- The Example Team\n");
    }

    private boolean above10k(KafkaConsumerRecord<String, JsonObject> record) {
        return record.value().getInteger("stepsCount") >= 10_000;
    }

    private Observable<Throwable> retryLater(Observable<Throwable> errs) {
        return errs.delay(10, TimeUnit.SECONDS, RxHelper.scheduler(vertx));
    }

}