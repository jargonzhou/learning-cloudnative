package com.spike.vertx.verticles;

import com.spike.vertx.test.TestSingleVerticleBase;
import io.vertx.core.AbstractVerticle;

public class TestMixedThreadingSingleVerticle extends TestSingleVerticleBase {
  @Override
  public AbstractVerticle createVerticle() {
    return new MixedThreadingVerticle();
  }

  @Override
  public long haltMs() {
    return 3_000;
  }
}
