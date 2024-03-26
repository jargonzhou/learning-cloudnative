package com.spike.vertx.ingestion;

import com.spike.vertx.commons.Constants;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.amqp.AmqpClient;
import io.vertx.rxjava3.amqp.AmqpMessage;
import io.vertx.rxjava3.amqp.AmqpReceiver;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducer;
import io.vertx.rxjava3.kafka.client.producer.KafkaProducerRecord;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class IngesterVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(IngesterVerticle.class);

    private KafkaProducer<String, JsonObject> updateProducer;

    @Override
    public Completable rxStart() {
        this.updateProducer = KafkaProducer.create(vertx, KafkaConfig.producer());

        AmqpClient.create(vertx, AMQPConfig.clientOptions())
                .rxConnect()
                .flatMap(conn -> conn.createReceiver(Constants.AMQP.Address.step_events,
                        AMQPConfig.receiverOptions()))
                .flatMapPublisher(AmqpReceiver::toFlowable)
                .doOnError(err -> LOG.error("ERROR", err))
                .retryWhen(this::retryLater)
                .subscribe(this::handleAmqpMessage);

        Router router = Router.router(vertx);
        router.post().handler(BodyHandler.create());
        router.post(Constants.IngestionService.HTTPApi.INGEST).handler(this::httpIngest);

        return vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(Constants.IngestionService.HTTP_PORT)
                .doOnSuccess(httpServer -> {
                    LOG.info("{} started on {}",
                            this.getClass().getSimpleName(), Constants.IngestionService.HTTP_PORT);
                })
                .doOnError(err -> {
                    LOG.error("{} failed to start on {}",
                            this.getClass().getSimpleName(), Constants.IngestionService.HTTP_PORT, err);
                })
                .ignoreElement();
    }

    private Publisher<Throwable> retryLater(Flowable<Throwable> err) {
        // ???
        return err.delay(10, TimeUnit.SECONDS, RxHelper.scheduler(vertx));
    }

    private void handleAmqpMessage(AmqpMessage amqpMessage) {
        if (!"application/json".equals(amqpMessage.contentType())
                || invalidIngestedJson(amqpMessage.bodyAsJsonObject())) {
            LOG.error("Invalid AMQP message: {}", amqpMessage.bodyAsBinary());
            amqpMessage.accepted();
            return;
        }
        JsonObject payload = amqpMessage.bodyAsJsonObject();
        KafkaProducerRecord<String, JsonObject> record = this.makeRecord(payload);
        this.updateProducer
                .rxSend(record)
                .subscribe(
                        rm -> amqpMessage.accepted(),
                        err -> {
                            LOG.error("AMQP ingestion to Kafka failed: {}",
                                    amqpMessage.bodyAsJsonObject());
                            amqpMessage.rejected();
                        }
                );
    }

    private void httpIngest(RoutingContext rc) {
        JsonObject payload = rc.body().asJsonObject();
        if (this.invalidIngestedJson(payload)) {
            LOG.warn("Invalid ingestion data: {}", payload.encode());
            rc.fail(400);
            return;
        }
        KafkaProducerRecord<String, JsonObject> kafkaRecord = this.makeRecord(payload);
        this.updateProducer.rxSend(kafkaRecord)
                .subscribe(
                        rm -> {
                            LOG.info("HTTP ingestion succeed:　{} {} {}",
                                    rm.getTopic(), rm.getPartition(), rm.getOffset());
                            rc.response().end();
                        },
                        err -> {
                            LOG.error("HTTP ingestion failed", err);
                            rc.fail(500);
                        });
    }

    private boolean invalidIngestedJson(JsonObject payload) {
        return !payload.containsKey("deviceId") || !payload.containsKey("deviceSync") || !payload.containsKey("stepsCount");
    }

    private KafkaProducerRecord<String, JsonObject> makeRecord(JsonObject payload) {
        String key = payload.getString("deviceId");
        JsonObject data = new JsonObject()
                .put("deviceId", key)
                .put("deviceSync", payload.getLong("deviceSync"))
                .put("stepsCount", payload.getInteger("stepsCount"));
        return KafkaProducerRecord.create(Constants.Kafka.Topic.incoming_steps, key, data);
    }
}
