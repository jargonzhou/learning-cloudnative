package com.spike.vertx;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(TestMainVerticle.class);
  private final Set<String> ids = new HashSet<>();

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    MainVerticle verticle = new MainVerticle();
    verticle.setPort(new Random().nextInt(10) + 18888);
    vertx.deployVerticle(verticle, testContext.succeeding(id -> {
      LOG.info("Deployment id: {}", id);
      ids.add(id);
      testContext.completeNow();
    }));
  }

  @AfterEach
  void undeploy_verticle(Vertx vertx, VertxTestContext testContext) {
    ids.forEach(vertx::undeploy);
    testContext.completeNow();
  }

//  @Test
//  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
//    testContext.completeNow();
//  }
}
