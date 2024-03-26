package com.spike.vertx.event;

import com.spike.vertx.support.Threads;
import org.junit.jupiter.api.Test;

public class TestMyEventLoop {
  @Test
  public void testEventLoop() {
    final MyEventLoop eventLoop = new MyEventLoop();
    new Thread(() -> {
      for (int n = 0; n < 6; n++) {
        Threads.delay(1000);
        eventLoop.dispatch(new MyEvent("tick", n));
      }
      eventLoop.dispatch(new MyEvent("stop", null));
    }).start();

    new Thread(() -> {
      Threads.delay(2500);
      eventLoop.dispatch(new MyEvent("hello", "there"));
      eventLoop.dispatch(new MyEvent("hello", "universe"));
    }).start();

    eventLoop.dispatch(new MyEvent("hello", "world!"));
    eventLoop.dispatch(new MyEvent("foo", "bar"));

    eventLoop.on("hello", s -> System.out.println("hello " + s))
      .on("tick", n -> System.out.println("tick #" + n))
      .on("stop", ignore -> eventLoop.stop())
      .run();

    System.out.println("Bye!");
  }
}
