package com.spike.vertx.config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

public class VerticleDeploymentSpec {
  private AbstractVerticle verticle;
  private DeploymentOptions options = new DeploymentOptions().setInstances(1);

  public VerticleDeploymentSpec() {
  }

  public VerticleDeploymentSpec verticle(AbstractVerticle verticle) {
    this.verticle = verticle;
    return this;
  }

  public AbstractVerticle verticle() {
    return this.verticle;
  }

  public VerticleDeploymentSpec options(DeploymentOptions options) {
    this.options = options;
    return this;
  }

  public DeploymentOptions options() {
    return this.options;
  }
}
