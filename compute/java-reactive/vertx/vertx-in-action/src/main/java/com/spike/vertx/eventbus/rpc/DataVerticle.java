package com.spike.vertx.eventbus.rpc;

import com.spike.vertx.eventbus.Destinations;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;

/**
 * A verticle expose {@link SensorDataService} and bina to an address.
 */
public class DataVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    // bind a service to an address
    new ServiceBinder(vertx)
      .setAddress(Destinations.SENSOR_DATA_SERVICE)
      // expose a service implementation
      .register(SensorDataService.class, SensorDataService.create(vertx));
  }
}
