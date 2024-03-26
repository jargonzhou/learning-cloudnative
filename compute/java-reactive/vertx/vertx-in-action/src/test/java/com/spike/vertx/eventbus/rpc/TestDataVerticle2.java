package com.spike.vertx.eventbus.rpc;

import com.spike.vertx.eventbus.Destinations;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class TestDataVerticle2 {
  private static final Logger LOG = LoggerFactory.getLogger(TestDataVerticle2.class);

  private SensorDataService dataService;

  @BeforeEach
  public void prepare(Vertx vertx, VertxTestContext context) {
    vertx.deployVerticle(new DataVerticle(), context.succeeding(id -> {
      dataService = SensorDataService.createProxy(vertx, Destinations.SENSOR_DATA_SERVICE);
      context.completeNow();
    }));
  }

  @Test
  public void noSensor(VertxTestContext context) {
    Checkpoint failToGet = context.checkpoint();
    Checkpoint zeroAvg = context.checkpoint();

    dataService.valueFor("abc", context.failing(err -> context.verify(() -> {
      LOG.error("Failed", err);
      Assertions.assertThat(err.getMessage().startsWith("No value has been observed for "));
      failToGet.flag();
    })));

    dataService.average(context.succeeding(data -> context.verify(() -> {
      LOG.info("data: {}", data);
      double avg = data.getDouble("average");
      Assertions.assertThat(avg).isCloseTo(0.0d, Assertions.withinPercentage(1.0d));
      zeroAvg.flag();
    })));
  }

  @Test
  public void withSensors(Vertx vertx, VertxTestContext context) {
    Checkpoint getValue = context.checkpoint();
    Checkpoint goodAvg = context.checkpoint();

    JsonObject s1 = new JsonObject().put("id", "abc").put("temp", 21.0d);
    JsonObject s2 = new JsonObject().put("id", "def").put("temp", 23.0d);

    vertx.eventBus()
      .publish(Destinations.SENSOR_UPDATES, s1)
      .publish(Destinations.SENSOR_UPDATES, s2);

    dataService.valueFor("abc", context.succeeding(data -> context.verify(() -> {
      LOG.info("data: {}", data);
      Assertions.assertThat(data.getString("sensorId")).isEqualTo("abc");
      Assertions.assertThat(data.getDouble("value")).isEqualTo(21.0d);
      getValue.flag();
    })));

    dataService.average(context.succeeding(data -> context.verify(() -> {
      LOG.info("data: {}", data);
      Assertions.assertThat(data.getDouble("average"))
        .isCloseTo(22.0, Assertions.withinPercentage(1.0d));
      goodAvg.flag();
    })));
  }
}
