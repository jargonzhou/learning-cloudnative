package com.spike.vertx.streams;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestKeyValueDatabaseStream {

  @Test
  void stream_read_write(Vertx vertx, VertxTestContext testContext) throws Throwable {
    KeyValueDatabaseStream databaseStream = new KeyValueDatabaseStream("sample.db");
    databaseStream.write(vertx);

    databaseStream.read(vertx);

    testContext.completeNow();
  }

  @Test
  void stream_read_write_pull(Vertx vertx, VertxTestContext testContext) throws Throwable {
    KeyValueDatabaseStream databaseStream = new KeyValueDatabaseStream("sample.db");
    databaseStream.write(vertx);

    databaseStream.readPull(vertx);

    testContext.completeNow();
  }

}
