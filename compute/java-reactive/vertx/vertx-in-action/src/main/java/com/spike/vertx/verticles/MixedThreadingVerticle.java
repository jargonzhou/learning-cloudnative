package com.spike.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * A verticle working with non-Vert.x thread.
 */
public class MixedThreadingVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(MixedThreadingVerticle.class);

  @Override
  public void start() throws Exception {
    new Thread(() -> {
      try {
        run(context);
      } catch (InterruptedException e) {
        LOG.error("Woops", e);
      }
    }).start();
  }

  private void run(Context context) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    LOG.info("I am in a non-Vert.x thread");
    context.runOnContext(v -> {
      LOG.info("I am on the event-loop");
      vertx.setTimer(1000, timerID -> {
        LOG.info("This is the final countdown");
        latch.countDown();
      });
    });

    LOG.info("Waiting ont he countdown latch...");
    latch.await();
    LOG.info("Bye!");
  }
}
