package com.spike.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

/**
 * A HTTP verticle expose '/' and '/sse'.
 */
public class HttpServerVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    vertx.createHttpServer()
      .requestHandler(this::handler)
      .listen(config().getInteger("port", 8080));
  }

  private void handler(HttpServerRequest request) {
    if ("/".equals(request.path())) {
      // HTML response
      request.response().sendFile("web/index.html");
    } else if ("/sse".equals(request.path())) {
      // sse response
      this.sse(request);
    } else {
      request.response().setStatusCode(404);
    }
  }

  // sever sent event
  private void sse(HttpServerRequest request) {
    HttpServerResponse response = request.response();
    response.putHeader("Content-Type", "text/event-stream")
      .putHeader("Cache-Control", "no-cache")
      .setChunked(true);

    MessageConsumer<JsonObject> consumer =
      vertx.eventBus().consumer(Destinations.SENSOR_UPDATES);
    consumer.handler(msg -> {
      response.write("event: update\n");
      response.write("data: " + msg.body().encode() + "\n\n");
    });

//    TimeoutStream timeoutStream = vertx.periodicStream(1000);
    long timerID = vertx.setPeriodic(1000, id -> {
      // request-reply
      vertx.eventBus().<JsonObject>request(Destinations.SENSOR_AVERAGE, "",
        reply -> {
          if (reply.succeeded()) {
            response.write("event: average\n");
            response.write("data: " + reply.result().body().encode() + "\n\n");
          }
        });
    });

    // unregister consumer and cancel timer
    response.endHandler(v -> {
      consumer.unregister();
      vertx.cancelTimer(timerID);
    });
  }
}
