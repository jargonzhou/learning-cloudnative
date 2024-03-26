package com.spike.vertx.verticles;

import com.spike.vertx.test.TestSingleVerticleBase;
import io.vertx.core.AbstractVerticle;

public class TestDeployerVerticleSingleVerticleBase extends TestSingleVerticleBase {
  @Override
  public AbstractVerticle createVerticle() {
    return new DeployerVerticle();
  }

  @Override
  public long haltMs() {
    return 20000;
  }
}
