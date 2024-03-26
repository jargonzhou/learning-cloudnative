package com.spike.vertx.eventbus;

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
import java.util.Set;

@ExtendWith(VertxExtension.class)
public class TestEventBusApplication {
  private static final Logger LOG = LoggerFactory.getLogger(TestEventBusApplication.class);
  protected Set<String> ids = new HashSet<>();

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

    vertx.deployVerticle(HeatSensorVerticle.class.getName(),
      new DeploymentOptions().setInstances(4), registerDeploymentIDHandler);
    vertx.deployVerticle(new ListenerVerticle(), registerDeploymentIDHandler);
    vertx.deployVerticle(new SensorDataVerticle(), registerDeploymentIDHandler);
    vertx.deployVerticle(new HttpServerVerticle(), completionHandler);
  }

  @AfterEach
  void undeploy_verticle(Vertx vertx, VertxTestContext testContext) {
    Threads.delay(60_000);
    LOG.info("Deployment IDs: {}", String.join(", ", ids));

    testContext.completeNow();
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }
}
