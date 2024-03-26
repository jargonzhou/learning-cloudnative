package com.spike.vertx.eventbus.rpc;

import com.spike.vertx.config.VerticleDeploymentSpec;
import com.spike.vertx.eventbus.Destinations;
import com.spike.vertx.eventbus.HeatSensorVerticle;
import com.spike.vertx.support.Threads;
import com.spike.vertx.test.TestMultipleVerticlesBase;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TestDataVerticle extends TestMultipleVerticlesBase {
  private static final Logger LOG = LoggerFactory.getLogger(TestDataVerticle.class);

  @Override
  public List<VerticleDeploymentSpec> createVerticles() {
    List<VerticleDeploymentSpec> specs = new ArrayList<>();

    specs.add(new VerticleDeploymentSpec()
      .verticle(new HeatSensorVerticle())
      .options(new DeploymentOptions().setInstances(4)));

    specs.add(new VerticleDeploymentSpec()
      .verticle(new DataVerticle()));
    return specs;
  }

  @Override
  public long haltMs() {
    return 30_000;
  }

  @Test
  public void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {

    // invoke the service
    SensorDataService service = SensorDataService.createProxy(vertx, Destinations.SENSOR_DATA_SERVICE);
    for (int i = 0; i < 3; i++) {
      Threads.delay(8_000);

      service.average(ar -> {
        if (ar.succeeded()) {
          LOG.info("Average = {}", ar.result());
        } else {
          LOG.error("Failed", ar.cause());
        }
      });

      service.sensors(ar -> {
        if (ar.succeeded()) {
          ar.result().forEach(sensorId -> {
            service.valueFor(sensorId, ar2 -> {
              if (ar2.succeeded()) {
                LOG.info("Sensor Data: {}", ar2.result());
              }
            });
          });
        }
      });
    }

    testContext.completeNow();
  }
}
