package com.spike.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A verticle deployed by {@link DeployerVerticle}.
 */
public class EmptyVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(EmptyVerticle.class);

  @Override
  public void start() throws Exception {
    LOG.info("Start");
  }

  @Override
  public void stop() throws Exception {
    LOG.info("Stop");
  }
}
