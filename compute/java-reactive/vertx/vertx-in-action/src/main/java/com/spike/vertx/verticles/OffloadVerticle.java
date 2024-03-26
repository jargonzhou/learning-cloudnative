package com.spike.vertx.verticles;

import com.spike.vertx.support.Threads;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * A verticle executed in {@link Vertx#executeBlocking(Callable, Handler)}
 */
public class OffloadVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(OffloadVerticle.class);

  @Override
  public void start() throws Exception {
    vertx.setPeriodic(3000, timerId -> {
      LOG.info("Tick");
      vertx.executeBlocking(() -> {
        LOG.info("Blocking code running");
        try {
          Threads.delayE(1000);
          LOG.info("Done!");
          return "Ok!";
        } catch (InterruptedException e) {
          return "Fail!";
        }
      });
    });
  }
}
