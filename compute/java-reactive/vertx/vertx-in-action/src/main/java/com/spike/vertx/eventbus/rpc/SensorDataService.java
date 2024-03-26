package com.spike.vertx.eventbus.rpc;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * A sensor data service interface.
 */
@ProxyGen
public interface SensorDataService {
  /**
   * Provide service implementation.
   */
  static SensorDataService create(Vertx vertx) {
    return new SensorDataServiceImpl(vertx);
  }

  /**
   * Provide serve proxy by address.
   */
  static SensorDataService createProxy(Vertx vertx, String address) {
    return new SensorDataServiceVertxEBProxy(vertx, address);
  }

  // maybe not a good method in case of large number of sensors.
  void sensors(Handler<AsyncResult<List<String>>> handler);

  void valueFor(String sensorId, Handler<AsyncResult<JsonObject>> handler);

  void average(Handler<AsyncResult<JsonObject>> handler);

}
