package com.spike.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

/**
 * A dumping sensor data verticle.
 */
public class ListenerVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(ListenerVerticle.class);
  private static final DecimalFormat format = new DecimalFormat("#.##");

  @Override
  public void start() throws Exception {
    EventBus eventBus = vertx.eventBus();
    eventBus.<JsonObject>consumer(Destinations.SENSOR_UPDATES, msg -> {
      JsonObject payload = msg.body();
      String id = payload.getString("id");
      String temperature = format.format(payload.getDouble("temp"));
      LOG.info("{} reports a temperature ~{}C", id, temperature);
    });
  }
}
