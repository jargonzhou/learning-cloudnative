package com.spike.vertx.eventstats;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumer;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.spike.vertx.commons.Constants.*;

/**
 * <li>Consume {@link Kafka.Topic#incoming_steps} to {@link Kafka.Topic#event_stats_throughput} every 5 seconds</li>
 * <li>Consume {@link Kafka.Topic#daily_step_updates} to {@link Kafka.Topic#event_stats_user_activity_updates}</li>
 * <li>Consumer {@link Kafka.Topic#event_stats_user_activity_updates} to {@link Kafka.Topic#event_stats_city_trends_update}</li>
 */
public class EventStatsVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(EventStatsVerticle.class);

    private WebClient webClient;
    private KafkaProducer<String, JsonObject> producer;

    @Override
    public Completable rxStart() {
        this.webClient = WebClient.create(vertx);
        this.producer = KafkaProducer.create(vertx, KafkaConfig.producer());

        KafkaConsumer<String, JsonObject> consumer1 = KafkaConsumer.create(vertx,
                KafkaConfig.consumer(EventStatsService.KafkaConsumerGroup.event_stats_throughput));
        consumer1.subscribe(Kafka.Topic.incoming_steps)
                .subscribe(
                        () -> consumer1.toObservable()
                                .buffer(5, TimeUnit.SECONDS, RxHelper.scheduler(vertx))
                                .flatMap(this::publishThroughput)
                                .doOnError(err -> LOG.error("ERROR", err))
                                .retryWhen(this::retryLater)
                                .subscribe(),
                        err -> LOG.error("Subscribe {} failed", Kafka.Topic.incoming_steps, err));


        KafkaConsumer<String, JsonObject> consumer2 = KafkaConsumer.create(vertx,
                KafkaConfig.consumer(EventStatsService.KafkaConsumerGroup.event_stats_user_activity_updates));
        consumer2
                .subscribe(Kafka.Topic.daily_step_updates)
                .subscribe(
                        () -> consumer2.toObservable()
                                .flatMap(this::addDeviceOwner)
                                .flatMap(this::addOwnerData)
                                .flatMap(this::publishUserActivityUpdate)
                                .doOnError(err -> LOG.error("ERROR", err))
                                .retryWhen(this::retryLater)
                                .subscribe(),
                        err -> LOG.error("Subscribe {} failed", Kafka.Topic.daily_step_updates, err)
                );

        KafkaConsumer<String, JsonObject> consumer3 = KafkaConsumer.create(vertx,
                KafkaConfig.consumer(EventStatsService.KafkaConsumerGroup.event_stats_city_trends));
        consumer3.subscribe(Kafka.Topic.event_stats_user_activity_updates)
                .subscribe(
                        () -> consumer3.toObservable()
                                .groupBy(this::city)
                                .flatMap(gf -> gf.buffer(5, TimeUnit.SECONDS, RxHelper.scheduler(vertx)))
                                .flatMap(this::publishCityTrends)
                                .doOnError(err -> LOG.error("ERROR", err))
                                .retryWhen(this::retryLater)
                                .subscribe(),
                        err -> LOG.error("Subscribe {} failed", Kafka.Topic.event_stats_user_activity_updates, err)
                );


        return Completable.complete();
    }

    private @NonNull Observable<Object> publishCityTrends(List<KafkaConsumerRecord<String, JsonObject>> records) {
        if (records.isEmpty()) {
            return Observable.empty();
        }

        String city = this.city(records.get(0));
        Long stepsCount = records.stream()
                .map(record -> record.value().getLong("stepsCount"))
                .reduce(0L, Long::sum);
        KafkaProducerRecord<String, JsonObject> record =
                KafkaProducerRecord.create(Kafka.Topic.event_stats_city_trends_update, city, new JsonObject()
                        .put("timestamp", LocalDateTime.now().toString())
                        .put("seconds", 5)
                        .put("city", city)
                        .put("stepsCount", stepsCount)
                        .put("updates", records.size()));
        return producer.rxWrite(record).toObservable();
    }

    private Observable<JsonObject> addDeviceOwner(KafkaConsumerRecord<String, JsonObject> record) {
        JsonObject data = record.value();
        String deviceId = data.getString("deviceId");
        return webClient.get(UserProfileService.HTTP_PORT, UserProfileService.HOST,
                        String.format(UserProfileService.HTTPApi.OWNS_DEVICE_ID, deviceId))
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(HttpResponse::body)
                .map(data::mergeIn);
    }

    private Observable<JsonObject> addOwnerData(JsonObject data) {
        String username = data.getString("username");
        return webClient.get(UserProfileService.HTTP_PORT, UserProfileService.HOST,
                        String.format(UserProfileService.HTTPApi.USERNAME, username))
                .as(BodyCodec.jsonObject())
                .rxSend()
                .toObservable()
                .map(HttpResponse::body)
                .map(data::mergeIn);
    }


    private Observable<Object> publishUserActivityUpdate(JsonObject data) {
        return producer.rxWrite(
                        KafkaProducerRecord.create(Kafka.Topic.event_stats_user_activity_updates,
                                data.getString("username"),
                                data))
                .toObservable();
    }

    private Observable<Object> publishThroughput(List<KafkaConsumerRecord<String, JsonObject>> records) {
        KafkaProducerRecord<String, JsonObject> record = KafkaProducerRecord.create(Kafka.Topic.event_stats_throughput,
                new JsonObject()
                        .put("seconds", 5)
                        .put("count", records.size())
                        .put("throughput", (((double) records.size()) / 5.0d)));
        return producer.rxWrite(record).toObservable();
    }

    private String city(KafkaConsumerRecord<String, JsonObject> record) {
        return record.value().getString("city");
    }

    private Observable<Throwable> retryLater(Observable<Throwable> err) {
        return err.delay(10, TimeUnit.SECONDS, RxHelper.scheduler(vertx));
    }
}