package com.spike.vertx.support;

public final class Threads {
  public static void delay(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void delayE(long ms) throws InterruptedException {
    Thread.sleep(ms);
  }
}
