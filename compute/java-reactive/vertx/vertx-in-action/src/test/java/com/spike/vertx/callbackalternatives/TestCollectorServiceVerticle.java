package com.spike.vertx.callbackalternatives;

import com.spike.vertx.config.VerticleDeploymentSpec;
import com.spike.vertx.test.TestMultipleVerticlesBase;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class TestCollectorServiceVerticle extends TestMultipleVerticlesBase {
  @Override
  public List<VerticleDeploymentSpec> createVerticles() {
    List<VerticleDeploymentSpec> specs = new ArrayList<>();
    specs.add(new VerticleDeploymentSpec()
      .verticle(new HeatSensorVerticle())
      .options(new DeploymentOptions().setConfig(
        new JsonObject()
          .put("http.port", HeatSensorVerticle.BASE_PORT))));
    specs.add(new VerticleDeploymentSpec()
      .verticle(new HeatSensorVerticle())
      .options(new DeploymentOptions().setConfig(
        new JsonObject()
          .put("http.port", HeatSensorVerticle.BASE_PORT + 1))));
    specs.add(new VerticleDeploymentSpec()
      .verticle(new HeatSensorVerticle())
      .options(new DeploymentOptions().setConfig(
        new JsonObject()
          .put("http.port", HeatSensorVerticle.BASE_PORT + 2))));

    specs.add(new VerticleDeploymentSpec()
      .verticle(new SnapshotServiceVerticle()));

    specs.add(new VerticleDeploymentSpec()
      .verticle(new CollectorServiceVerticle()));

    return specs;
  }

  @Override
  public long haltMs() {
    return 60_000;
  }
}
