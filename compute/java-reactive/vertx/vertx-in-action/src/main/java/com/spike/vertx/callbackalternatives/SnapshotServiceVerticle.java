package com.spike.vertx.callbackalternatives;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A mocking snapshot verticle.
 */
public class SnapshotServiceVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(SnapshotServiceVerticle.class);

  public static final int BASE_PORT = 4000;

  @Override
  public void start() throws Exception {
    vertx.createHttpServer()
      .requestHandler(request -> {
        if (this.badRequest(request)) {
          request.response().setStatusCode(400).end();
        }
        request.bodyHandler(buffer -> {
          LOG.info("Latest Temperatures: {}",
            buffer.toJsonObject().encodePrettily());
          request.response().end();
        });
      })
      .listen(config().getInteger("http.port", BASE_PORT));
  }

  private boolean badRequest(HttpServerRequest request) {
    return !HttpMethod.POST.equals(request.method()) ||
      !"application/json".equals(request.getHeader("Content-Type"));
  }
}
