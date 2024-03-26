package com.spike.vertx.eventbus.rpc;

import com.spike.vertx.eventbus.Destinations;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A sensor data service interface implementation over event bus.
 */
public class SensorDataServiceImpl implements SensorDataService {
  private final Map<String, Double> lastValues = new HashMap<>();

  public SensorDataServiceImpl(Vertx vertx) {
    vertx.eventBus().<JsonObject>consumer(Destinations.SENSOR_UPDATES, message -> {
      JsonObject payload = message.body();
      lastValues.put(payload.getString("id"), payload.getDouble("temp"));
    });
  }

  @Override
  public void sensors(Handler<AsyncResult<List<String>>> handler) {
    if (lastValues.isEmpty()) {
      handler.handle(Future.failedFuture("No sensor yet"));
    } else {
      handler.handle(Future.succeededFuture(lastValues.keySet().stream().toList()));
    }
  }

  @Override
  public void valueFor(String sensorId, Handler<AsyncResult<JsonObject>> handler) {
    if (lastValues.containsKey(sensorId)) {
      JsonObject data = new JsonObject()
        .put("sensorId", sensorId)
        .put("value", lastValues.get(sensorId));
      handler.handle(Future.succeededFuture(data));
    } else {
      handler.handle(Future.failedFuture("No value has been observed for " + sensorId));
    }
  }

  @Override
  public void average(Handler<AsyncResult<JsonObject>> handler) {
    double avg = lastValues.values().stream()
      .collect(Collectors.averagingDouble(Double::doubleValue));
    JsonObject data = new JsonObject().put("average", avg);
    handler.handle(Future.succeededFuture(data));
  }
}
