package com.spike.vertx.verticles;

import com.spike.vertx.test.TestSingleVerticleBase;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;

public class TestWorkerSingleVerticleBase extends TestSingleVerticleBase {
  @Override
  public AbstractVerticle createVerticle() {
    return new WorkerVerticle();
  }

  @Override
  protected DeploymentOptions withOptions() {
    return new DeploymentOptions()
      .setInstances(2)
//      .setWorker(true);
      .setThreadingModel(ThreadingModel.WORKER);
  }

  @Override
  public long haltMs() {
    return 10_000;
  }
}
