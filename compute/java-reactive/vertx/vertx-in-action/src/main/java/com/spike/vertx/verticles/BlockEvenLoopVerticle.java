package com.spike.vertx.verticles;

import com.spike.vertx.support.Threads;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
//import io.vertx.core.VertxOptions;

/**
 * A verticle to block the event loop to trigger block thread checking.
 */
public class BlockEvenLoopVerticle extends AbstractVerticle {
  public void start(Promise<Void> startPromise) throws Exception {
    // blocked thread check interval: VertxOptions.DEFAULT_BLOCKED_THREAD_CHECK_INTERVAL
    vertx.setTimer(1000, id -> {
//      while (true) ;
      Threads.delay(5000);
    });
    startPromise.complete();
  }

}
