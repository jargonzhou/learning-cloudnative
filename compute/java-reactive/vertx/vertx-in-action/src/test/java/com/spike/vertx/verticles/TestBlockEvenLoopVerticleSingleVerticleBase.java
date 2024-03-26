package com.spike.vertx.verticles;

import com.spike.vertx.test.TestSingleVerticleBase;
import io.vertx.core.AbstractVerticle;

public class TestBlockEvenLoopVerticleSingleVerticleBase extends TestSingleVerticleBase {
  @Override
  public AbstractVerticle createVerticle() {
    return new BlockEvenLoopVerticle();
  }

  @Override
  public long haltMs() {
    return 10000;
  }
}
