package com.spike.vertx.event;

public class MyEvent {
  private final String key;
  private final Object data;

  public MyEvent(String key, Object data) {
    this.key = key;
    this.data = data;
  }

  public String getKey() {
    return key;
  }

  public Object getData() {
    return data;
  }
}
