package com.spike.vertx.test;

import com.spike.vertx.config.VerticleDeploymentSpec;
import com.spike.vertx.eventbus.TestEventBusApplication;
import com.spike.vertx.support.Threads;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(VertxExtension.class)
public abstract class TestMultipleVerticlesBase {
  private static final Logger LOG = LoggerFactory.getLogger(TestMultipleVerticlesBase.class);
  protected Set<String> ids = new HashSet<>();

  public abstract List<VerticleDeploymentSpec> createVerticles();

  public abstract long haltMs();

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    Handler<AsyncResult<String>> registerDeploymentIDHandler = testContext.succeeding(id -> {
      LOG.info("Deployment id: {}", id);
      ids.add(id);
    });

    Handler<AsyncResult<String>> completionHandler = testContext.succeeding(id -> {
      LOG.info("Deployment id: {}", id);
      ids.add(id);
      testContext.completeNow();
    });

    vertx.getOrCreateContext().exceptionHandler(t -> {
      LOG.error("Woops", t);
    });

    List<VerticleDeploymentSpec> verticleSpecs = this.createVerticles();
    if (verticleSpecs == null || verticleSpecs.isEmpty()) {
      testContext.failed();
      return;
    }

    int size = verticleSpecs.size();
    VerticleDeploymentSpec verticleSpec;
    DeploymentOptions options;
    for (int i = 0; i < size - 1; i++) {
      verticleSpec = verticleSpecs.get(i);
      options = verticleSpec.options();
      boolean multipleInstanceFlag = options != null && options.getInstances() > 0;
      if (multipleInstanceFlag) {
        vertx.deployVerticle(verticleSpec.verticle().getClass().getName(), options, registerDeploymentIDHandler);
      } else {
        if (options == null) options = new DeploymentOptions();
        vertx.deployVerticle(verticleSpec.verticle(), options, registerDeploymentIDHandler);
      }
    }
    verticleSpec = verticleSpecs.get(size - 1);
    options = verticleSpec.options();
    boolean multipleInstanceFlag = options != null && options.getInstances() > 0;
    if (multipleInstanceFlag) {
      vertx.deployVerticle(verticleSpec.verticle().getClass().getName(), completionHandler);
    } else {
      if (options == null) options = new DeploymentOptions();
      vertx.deployVerticle(verticleSpec.verticle(), completionHandler);
    }

  }

  @AfterEach
  void undeploy_verticle(Vertx vertx, VertxTestContext testContext) {
    final long delayMs = this.haltMs();
    if (delayMs > 0) {
      Threads.delay(delayMs);
    }
    LOG.info("Deployment IDs: {}", String.join(", ", ids));

    testContext.completeNow();
  }

  @Test
  public void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }
}
