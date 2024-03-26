package com.spike.vertx.callbackalternatives;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A collector verticle to collect data from {@link HeatSensorVerticle} and send to {@link SnapshotServiceVerticle}.
 */
public class CollectorServiceFPVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(CollectorServiceFPVerticle.class);

  public static final int BASE_PORT = 8080;
  private WebClient webClient;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    webClient = WebClient.create(vertx);

    vertx.createHttpServer()
      .requestHandler(this::handleRequest)
      .listen(BASE_PORT)
      .onFailure(startPromise::fail)
      .onSuccess(httpServer -> {
        LOG.info("{} start on {}", this.getClass().getSimpleName(), BASE_PORT);
        startPromise.complete();
      });
  }

  private void handleRequest(HttpServerRequest request) {
    Future.all(
        this.fetchTemperature(HeatSensorVerticle.BASE_PORT),
        this.fetchTemperature(HeatSensorVerticle.BASE_PORT + 1),
        this.fetchTemperature(HeatSensorVerticle.BASE_PORT + 2)
      ).flatMap(this::sendToSnapshot)
      .onSuccess(data -> request.response()
        .putHeader("Content-Type", "application/json")
        .end(data.encode()))
      .onFailure(err -> {
        LOG.error("Handle request failed", err);
        request.response().setStatusCode(500).end();
      });
  }

  private Future<JsonObject> sendToSnapshot(CompositeFuture tempFuture) {
    List<JsonObject> temps = tempFuture.list();
    JsonObject data = new JsonObject()
      .put("data", new JsonArray()
        .add(temps.get(0))
        .add(temps.get(1))
        .add(temps.get(2)));
    return webClient.post(SnapshotServiceVerticle.BASE_PORT, "localhost", "/")
      .expect(ResponsePredicate.SC_SUCCESS)
      .sendJson(data)
      .map(response -> data);
  }

  private Future<JsonObject> fetchTemperature(int port) {
    return webClient.get(port, "localhost", "/")
      .expect(ResponsePredicate.SC_SUCCESS)
      .as(BodyCodec.jsonObject())
      .send()
      .map(HttpResponse::body);
  }
}
