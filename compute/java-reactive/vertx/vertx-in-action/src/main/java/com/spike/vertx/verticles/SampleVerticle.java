package com.spike.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A verticle using configurations.
 */
public class SampleVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(SampleVerticle.class);

  @Override
  public void start() throws Exception {
    LOG.info("n = {}", config().getInteger("n", -1));
  }

}
