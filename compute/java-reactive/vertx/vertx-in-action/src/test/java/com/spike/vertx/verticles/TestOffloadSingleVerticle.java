package com.spike.vertx.verticles;

import com.spike.vertx.test.TestSingleVerticleBase;
import io.vertx.core.AbstractVerticle;

public class TestOffloadSingleVerticle extends TestSingleVerticleBase {
  @Override
  public AbstractVerticle createVerticle() {
    return new OffloadVerticle();
  }

  @Override
  public long haltMs() {
    return 5_000;
  }
}
