package com.spike.vertx.activity;

import com.spike.vertx.commons.Constants;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgException;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumer;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducerRecord;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.rxjava3.sqlclient.SqlClient;
import io.vertx.rxjava3.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * <li>Consume {@link Constants.Kafka.Topic#incoming_steps}</li>
 * <li>Save to PostgreSQL stepevent</li>
 * <li>Produce {@link Constants.Kafka.Topic#daily_step_updates} with stats on PostgreSQL stepevent</li>
 */
public class ActivityEventVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(ActivityEventVerticle.class);

    private KafkaConsumer<String, JsonObject> eventConsumer;
    private KafkaProducer<String, JsonObject> updateProducer;
    private SqlClient sqlClient;

    @Override
    public Completable rxStart() {
        this.eventConsumer = KafkaConsumer.create(vertx,
                KafkaConfig.consumer(Constants.ActivityService.KAFKA_CONSUMER_GROUP_ID));
        this.updateProducer = KafkaProducer.create(vertx, KafkaConfig.producer());

        this.eventConsumer.subscribe(Constants.Kafka.Topic.incoming_steps)
                .doOnComplete(() -> {
                    this.eventConsumer
                            .toObservable()
                            .flatMap(this::insertRecord)
                            .flatMap(this::generateActivityUpdate)
                            .flatMap(this::commitKafkaConsumerOffset)
                            .doOnError(err -> LOG.error("ERROR", err))
                            .retryWhen(this::retryLater)
                            .subscribe();
                })
                .subscribe(
                        () -> LOG.info("Subscribe Done"),
                        err -> LOG.error("Subscribe ERROR", err));

        this.sqlClient = PgBuilder.client()
                .using(vertx)
                .connectingTo(PgConfig.options())
                .with(PgConfig.poolOptions())
                .build();

        return Completable.complete();
    }

    private Observable<KafkaConsumerRecord<String, JsonObject>>
    insertRecord(KafkaConsumerRecord<String, JsonObject> record) {
        JsonObject data = record.value();
        Tuple values = Tuple.of(
                data.getString("deviceId"),
                data.getLong("deviceSync"),
                data.getInteger("stepsCount"));
        return sqlClient.preparedQuery(PgSQLs.insertStepEvent())
                .rxExecute(values)
                .map(rs -> record) // return input
                .onErrorReturn(err -> {
                    if (this.duplicateKeyInsert(err)) {
                        return record;
                    } else {
                        LOG.error("Insert stepevent failed", err);
                        throw new RuntimeException(err);
                    }
                })
                .toObservable();
    }

    private boolean duplicateKeyInsert(Throwable err) {
        return (err instanceof PgException) && "23505".equals(((PgException) err).getSqlState());
    }


    private Observable<KafkaConsumerRecord<String, JsonObject>> generateActivityUpdate(KafkaConsumerRecord<String, JsonObject> record) {
        String deviceId = record.value().getString("deviceId");
        LocalDateTime now = LocalDateTime.now();
        String key = deviceId + ":" + now.getYear() + "-" + now.getMonth() + "-" + now.getDayOfMonth();
        return sqlClient.preparedQuery(PgSQLs.stepsCountForToday())
                .rxExecute(Tuple.of(deviceId))
                .map(rs -> rs.iterator().next())
                .map(row -> new JsonObject()
                        .put("deviceId", deviceId)
                        .put("timestamp", row.getTemporal(0).toString())
                        .put("stepsCount", row.getLong(1)))
                .flatMap(json -> this.updateProducer.rxSend(
                        KafkaProducerRecord.create(Constants.Kafka.Topic.daily_step_updates, key, json)))
                .map(rs -> record) // return input
                .toObservable();
    }

    private @NonNull Observable<Object> commitKafkaConsumerOffset(KafkaConsumerRecord<String, JsonObject> record) {
        return this.eventConsumer.rxCommit().toObservable();
    }

    private Observable<Throwable> retryLater(Observable<Throwable> errs) {
        return errs.delay(10, TimeUnit.SECONDS, RxHelper.scheduler(vertx));
    }
}
