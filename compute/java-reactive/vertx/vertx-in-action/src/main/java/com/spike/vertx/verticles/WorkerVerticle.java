package com.spike.vertx.verticles;

import com.spike.vertx.support.Threads;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A verticle executing blocking codes on worker thread pool.
 */
public class WorkerVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(WorkerVerticle.class);

  @Override
  public void start() throws Exception {
    vertx.setPeriodic(3_000, timerID -> {
      LOG.info("Zzz...");
      try {
        Threads.delayE(1_000);
        LOG.info("Up!");
      } catch (InterruptedException e) {
        LOG.error("Woops", e);
      }
    });
  }
}
