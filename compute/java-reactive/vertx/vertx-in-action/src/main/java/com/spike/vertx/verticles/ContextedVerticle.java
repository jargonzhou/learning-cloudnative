package com.spike.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A verticle working with {@link io.vertx.core.Context}.
 */
public class ContextedVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(ContextedVerticle.class);

  @Override
  public void start() throws Exception {
    context.put("foo", "bar");

    // exception handler
    context.exceptionHandler(t -> {
      if ("Tada".equals(t.getMessage())) {
        LOG.info("Got a Tada exception");
      } else {
        LOG.error("Woops", t);
      }
    });

    context.runOnContext(v -> {
      throw new RuntimeException("Tada");
    });

    context.runOnContext(v -> {
      LOG.info("foo = {}", (String) context.get("foo"));
    });
  }
}
