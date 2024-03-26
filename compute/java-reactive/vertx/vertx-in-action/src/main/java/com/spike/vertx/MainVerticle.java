package com.spike.vertx;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
  private int port = 8888;

  // event handlers are always executed on the same thread, so no need synchronous.
  private int numberOfConnections = 0;

  public void setPort(int port) {
    this.port = port;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigStoreOptions store = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(new JsonObject()
        .put("path", "vertx.yaml")
      );

    ConfigRetriever retriever = ConfigRetriever.create(vertx,
      new ConfigRetrieverOptions().addStore(store));
    retriever.getConfig().onComplete(ar -> {
      if (ar.failed()) {
        LOG.error("Failed to retrieve the configuration", ar.cause());
        startPromise.fail(ar.cause());
      } else {
        JsonObject config = ar.result();
        LOG.info("Configuration: {}", config.toString());
        this.port = config.getInteger("server.port");

        this.launchServers(startPromise);
      }
    });

  }

  private void launchServers(Promise<Void> startPromise) {
    // Net server
    vertx.createNetServer()
      .connectHandler(this::handleNetConnect)
      .listen(this.port - 1, net -> {
        if (net.succeeded()) {
          LOG.info("Net server started on port " + (this.port - 1));
        } else {
          LOG.info("Net server started on port " + (this.port - 1) + " failed", net.cause());
          startPromise.fail(net.cause());
        }
      });

    vertx.setPeriodic(5000, timerId ->
      LOG.info(timerId + ": " + numberOfConnections + " connections now."));

    // HTTP server
    vertx.createHttpServer()
      .requestHandler(req -> {
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x! " + numberOfConnections + " connections now.");
      }).listen(this.port, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("HTTP server started on port " + this.port);
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private void handleNetConnect(NetSocket netSocket) {
    numberOfConnections++;

    netSocket.handler(buffer -> {
      netSocket.write(buffer);
      if (buffer.toString().contains("/quit")) {
        netSocket.close();
      }
    });

    netSocket.closeHandler(v -> numberOfConnections--);
  }
}
