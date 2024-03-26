package com.spike.vertx.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public final class MyEventLoop {
  private final ConcurrentLinkedDeque<MyEvent> eventQueue = new ConcurrentLinkedDeque<>();
  private final ConcurrentMap<String, Consumer<Object>> handlerMap = new ConcurrentHashMap<>();

  public MyEventLoop on(String key, Consumer<Object> handler) {
    this.handlerMap.put(key, handler);
    return this;
  }

  public void dispatch(MyEvent event) {
    this.eventQueue.add(event);
  }

  public void stop() {
    Thread.currentThread().interrupt();
  }

  public void run() {
    while (!(eventQueue.isEmpty() && Thread.interrupted())) {
      if (!eventQueue.isEmpty()) {
        MyEvent event = eventQueue.pop();
        if (handlerMap.containsKey(event.getKey())) {
          handlerMap.get(event.getKey()).accept(event.getData());
        } else {
          System.out.println("No handler for key " + event.getKey());
        }
      }
    }
  }
}
