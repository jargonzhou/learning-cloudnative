package com.spike.vertx.support;

import java.util.concurrent.ThreadLocalRandom;

public final class Ports {
  private Ports() {
  }

  public static int newPort() {
    return ThreadLocalRandom.current().nextInt(10000, 20000);
  }

}
