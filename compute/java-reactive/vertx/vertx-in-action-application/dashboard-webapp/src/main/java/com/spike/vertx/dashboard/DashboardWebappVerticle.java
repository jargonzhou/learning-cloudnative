package com.spike.vertx.dashboard;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;
import io.vertx.rxjava3.ext.web.handler.StaticHandler;
import io.vertx.rxjava3.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumer;
import io.vertx.rxjava3.kafka.client.consumer.KafkaConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.spike.vertx.commons.Constants.*;

public class DashboardWebappVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(DashboardWebappVerticle.class);

    private Map<String, JsonObject> publicRanking = new HashMap<>();

    @Override
    public Completable rxStart() {

        KafkaConsumer.<String, JsonObject>create(vertx,
                        KafkaConfig.consumer(DashboardWebapp.KafkaConsumerGroup.dashboard_webapp_throughput))
                .subscribe(Kafka.Topic.event_stats_throughput)
                .<KafkaConsumerRecord<String, JsonObject>>toFlowable()
                .subscribe(record -> sinkToEventBus(EventBus.Destination.client_updates_throughput, record));

        KafkaConsumer.<String, JsonObject>create(vertx,
                        KafkaConfig.consumer(DashboardWebapp.KafkaConsumerGroup.dashboard_webapp_city_trend))
                .subscribe(Kafka.Topic.event_stats_city_trends_update)
                .<KafkaConsumerRecord<String, JsonObject>>toFlowable()
                .subscribe(record -> sinkToEventBus(EventBus.Destination.client_updates_city_trend, record));

        KafkaConsumer.<String, JsonObject>create(vertx,
                        KafkaConfig.consumer(DashboardWebapp.KafkaConsumerGroup.dashboard_webapp_ranking))
                .subscribe(Kafka.Topic.event_stats_user_activity_updates)
                .<KafkaConsumerRecord<String, JsonObject>>toFlowable()
                .filter(record -> record.value().getBoolean("makePublic"))
                .buffer(5, TimeUnit.SECONDS, RxHelper.scheduler(vertx))
                .subscribe(this::updatePublicRanking);

        initPublicRanking();

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        sockJSHandler.bridge(new io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress(EventBus.Destination.INBOUND_ALLOWED_PATTERN))
                .addOutboundPermitted(new PermittedOptions().setAddress(EventBus.Destination.OUTBOUND_ALLOWED_PATTERN)));
        Router router = Router.router(vertx);
        router.route("/eventbus/*").handler(sockJSHandler);

        router.route().handler(StaticHandler.create("webapp/assets"));
        router.route("/*").handler(rc -> rc.reroute("/index.html"));


        return Completable.complete();
    }

    private void initPublicRanking() {
        WebClient webClient = WebClient.create(vertx);
        webClient.get(ActivityService.HTTP_PORT, ActivityService.HOST, ActivityService.HTTPApi.RANKING)
                .as(BodyCodec.jsonArray())
                .rxSend()
                .map(HttpResponse::bodyAsJsonArray)
                .flattenAsFlowable(Functions.identity())
                .cast(JsonObject.class)
                .flatMapSingle(json -> whoOwnsDevice(webClient, json))
                .flatMapSingle(json -> fillWithUserProfile(webClient, json))
                .subscribe(data -> {
                            if (data.getBoolean("makePublic")) {
                                publicRanking.put(data.getString("username"), data);
                            }
                        },
                        err -> LOG.error("ERROR", err),
                        () -> LOG.info("Done!"));
    }

    private Single<JsonObject> fillWithUserProfile(WebClient webClient, JsonObject json) {
        return webClient
                .get(3000, "localhost", "/" + json.getString("username"))
                .as(BodyCodec.jsonObject())
                .rxSend()
                .retry(5)
                .map(HttpResponse::body)
                .map(resp -> resp.mergeIn(json));
    }

    private Single<JsonObject> whoOwnsDevice(WebClient webClient, JsonObject json) {
        return webClient
                .get(3000, "localhost", "/owns/" + json.getString("deviceId"))
                .as(BodyCodec.jsonObject())
                .rxSend()
                .retry(5)
                .map(HttpResponse::body)
                .map(resp -> resp.mergeIn(json));
    }

    private void updatePublicRanking(List<KafkaConsumerRecord<String, JsonObject>> records) {
        for (KafkaConsumerRecord<String, JsonObject> record : records) {
            JsonObject data = record.value();
            String username = data.getString("username");
            Integer stepsCount = data.getInteger("stepsCount");
            if (publicRanking.containsKey(username)) {
                if (publicRanking.get(username).getInteger("stepsCount") < stepsCount) {
                    publicRanking.put(username, data);
                }
            } else {
                publicRanking.put(username, data);
            }
            Instant now = Instant.now();
            publicRanking = publicRanking.entrySet()
                    .stream().filter(e -> {
                        Instant timestamp = Instant.parse(e.getValue().getString("timestamp"));
                        return timestamp.until(now, ChronoUnit.DAYS) <= 1; // within 24 hours
                    }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        vertx.eventBus().publish(EventBus.Destination.client_updates_publicRanking, this.computePublicRanking());
    }

    private JsonArray computePublicRanking() {
        return new JsonArray(publicRanking.values().stream()
                .sorted((o1, o2) -> o2.getInteger("stepsCount").compareTo(o1.getInteger("stepsCount")))
                .map(json -> new JsonObject()
                        .put("username", json.getString("username"))
                        .put("stepsCount", json.getInteger("stepsCount"))
                        .put("city", json.getString("city")))
                .collect(Collectors.toList()));
    }

    private void sinkToEventBus(String destination, KafkaConsumerRecord<String, JsonObject> record) {
        vertx.eventBus().send(destination, record.value());
    }

}