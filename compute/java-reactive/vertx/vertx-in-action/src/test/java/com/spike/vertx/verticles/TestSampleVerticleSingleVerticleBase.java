package com.spike.vertx.verticles;

import com.spike.vertx.test.TestSingleVerticleBase;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

public class TestSampleVerticleSingleVerticleBase extends TestSingleVerticleBase {
  @Override
  public AbstractVerticle createVerticle() {
    return new SampleVerticle();
  }

  protected DeploymentOptions withOptions() {
    JsonObject config = new JsonObject().put("n", 1);
    return new DeploymentOptions()
      .setConfig(config)
      .setInstances(2);
  }

  @Override
  public long haltMs() {
    return 5000;
  }
}
