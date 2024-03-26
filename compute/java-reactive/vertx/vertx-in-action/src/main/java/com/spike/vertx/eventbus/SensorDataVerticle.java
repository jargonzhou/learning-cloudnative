package com.spike.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A verticle holding last sensor date.
 */
public class SensorDataVerticle extends AbstractVerticle {
  private final Map<String, Double> lastValues = new HashMap<>();

  @Override
  public void start() throws Exception {
    EventBus eventBus = vertx.eventBus();
    eventBus.consumer(Destinations.SENSOR_UPDATES, this::update);
    eventBus.consumer(Destinations.SENSOR_AVERAGE, this::average);
  }

  private void update(Message<JsonObject> message) {
    JsonObject payload = message.body();
    lastValues.put(payload.getString("id"), payload.getDouble("temp"));
  }

  private void average(Message<JsonObject> message) {
    double avg = lastValues.values().stream()
      .collect(Collectors.averagingDouble(Double::doubleValue));
    JsonObject payload = new JsonObject().put("average", avg);
    message.reply(payload); // replay a message
  }
}
