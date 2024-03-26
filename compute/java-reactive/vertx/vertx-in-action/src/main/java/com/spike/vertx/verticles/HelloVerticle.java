package com.spike.vertx.verticles;

import com.spike.vertx.support.Ports;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(HelloVerticle.class);
  private long counter = 1;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.setPeriodic(5000, id -> {
      LOG.info("tick");
    });

    final int port = Ports.newPort();
    vertx.createHttpServer()
      .requestHandler(req -> {
        LOG.info("Request #{} from {}", counter++,
          req.remoteAddress().host());
        req.response().end("Hello!");
      })
      .listen(port, http -> {
        if (http.succeeded()) {
          LOG.info("HTTP Server started on {}", port);
          startPromise.complete();
        } else {
          LOG.error("HTTP Server started on {}", port, http.cause());
          startPromise.fail(http.cause());
        }
      });

  }
}
