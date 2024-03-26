package com.spike.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A verticle deploying{@link EmptyVerticle}.
 */
public class DeployerVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(DeployerVerticle.class);

  @Override
  public void start() throws Exception {
    long delay = 1000;
    for (int i = 0; i < 10; i++) {
      vertx.setTimer(delay, timerID -> deploy());
      delay = delay + 1000;
    }
  }

  private void deploy() {
    vertx.deployVerticle(new EmptyVerticle(), ar -> {
      if (ar.succeeded()) {
        final String deploymentID = ar.result();
        LOG.info("Successfully deployed {}", deploymentID);
        vertx.setTimer(5000, timerID -> undeploy(deploymentID));
      } else {

      }
    });
  }

  private void undeploy(String deploymentID) {
    vertx.undeploy(deploymentID, ar -> {
      if (ar.succeeded()) {
        LOG.info("{} was undeployed", deploymentID);
      } else {
        LOG.error("{} could not be undeployed", deploymentID);
      }
    });
  }

  @Override
  public void stop() throws Exception {
    LOG.info("Stop");
  }
}
