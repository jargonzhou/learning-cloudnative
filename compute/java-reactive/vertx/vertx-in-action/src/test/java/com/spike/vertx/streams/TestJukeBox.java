package com.spike.vertx.streams;

import com.spike.vertx.config.VerticleDeploymentSpec;
import com.spike.vertx.test.TestMultipleVerticlesBase;

import java.util.ArrayList;
import java.util.List;

public class TestJukeBox extends TestMultipleVerticlesBase {
  @Override
  public List<VerticleDeploymentSpec> createVerticles() {
    List<VerticleDeploymentSpec> specs = new ArrayList<>();
    specs.add(new VerticleDeploymentSpec()
      .verticle(new JukeBoxVerticle()));
    specs.add(new VerticleDeploymentSpec()
      .verticle(new NetControlVerticle()));
    return specs;
  }

  @Override
  public long haltMs() {
    return 120_000;
  }
}
