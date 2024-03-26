package com.spike.vertx.callbackalternatives;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A collector verticle to collect data from {@link HeatSensorVerticle} and send to {@link SnapshotServiceVerticle}.
 */
public class CollectorServiceVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(CollectorServiceVerticle.class);

  private WebClient webClient;

  @Override
  public void start() throws Exception {
    webClient = WebClient.create(vertx);

    vertx.createHttpServer()
      .requestHandler(this::handleRequest)
      .listen(8080);
  }

  private void handleRequest(HttpServerRequest request) {
    List<JsonObject> responses = new ArrayList<>();
    AtomicInteger counter = new AtomicInteger(0);
    for (int i = 0; i < 3; i++) {
      final int sensorPort = HeatSensorVerticle.BASE_PORT + i;
      webClient.get(sensorPort, "localhost", "/")
        .expect(ResponsePredicate.SC_SUCCESS)
        .as(BodyCodec.jsonObject())
        .send(ar -> {
          if (ar.succeeded()) {
            responses.add(ar.result().body());
          } else {
            LOG.error("Sensor {} down?", sensorPort, ar.cause());
          }

          if (counter.incrementAndGet() == 3) {
            JsonObject data = new JsonObject()
              .put("data", new JsonArray(responses));
            this.sendToSnapshot(request, data);
          }
        });
    }
  }

  private void sendToSnapshot(HttpServerRequest request, JsonObject data) {
    webClient.post(SnapshotServiceVerticle.BASE_PORT, "localhost", "/")
      .expect(ResponsePredicate.SC_SUCCESS)
      .sendJson(data, ar -> {
        if (ar.succeeded()) {
          this.sendResponse(request, data);
        } else {
          LOG.error("Snapshot down?", ar.cause());
          request.response().setStatusCode(500).end();
        }
      });
  }

  private void sendResponse(HttpServerRequest request, JsonObject data) {
    request.response()
      .putHeader("Content-Type", "application/json")
      .end(data.encode());
  }
}
