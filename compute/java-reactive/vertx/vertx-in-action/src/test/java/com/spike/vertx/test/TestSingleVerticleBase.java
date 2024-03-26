package com.spike.vertx.test;

import com.spike.vertx.support.Threads;
import io.vertx.core.*;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(VertxExtension.class)
public abstract class TestSingleVerticleBase {
  private static final Logger LOG = LoggerFactory.getLogger(TestSingleVerticleBase.class);
  protected Set<String> ids = new HashSet<>();

  public abstract AbstractVerticle createVerticle();

  protected DeploymentOptions withOptions() {
    return new DeploymentOptions().setInstances(1);
  }

  public abstract long haltMs();

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    DeploymentOptions options = withOptions();
    if (options == null) {
      options = new DeploymentOptions();
    }
    final int instanceCount = options.getInstances();
    Handler<AsyncResult<String>> completionHandler = testContext.succeeding(id -> {
      LOG.info("Deployment id: {}", id);
      ids.add(id);
      testContext.completeNow();
    });
    if (instanceCount > 1) {
      this.doDeploy(vertx, this.createVerticle().getClass().getName(), options, completionHandler);
    } else {
      this.doDeploy(vertx, this.createVerticle(), options, completionHandler);
    }
  }

  private void doDeploy(Vertx vertx, AbstractVerticle verticle, DeploymentOptions options, Handler<AsyncResult<String>> completionHandler) {
    vertx.deployVerticle(verticle, options, completionHandler);
  }

  private void doDeploy(Vertx vertx, String verticleClassName, DeploymentOptions options, Handler<AsyncResult<String>> completionHandler) {
    vertx.deployVerticle(verticleClassName, options, completionHandler);
  }

  @AfterEach
  void undeploy_verticle(Vertx vertx, VertxTestContext testContext) {
    final long delayMs = this.haltMs();
    if (delayMs > 0) {
      Threads.delay(delayMs);
    }
    ids.forEach(id -> {
      LOG.info("Undeploy id: {}", id);
      vertx.undeploy(id);
    });
    testContext.completeNow();
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }
}
