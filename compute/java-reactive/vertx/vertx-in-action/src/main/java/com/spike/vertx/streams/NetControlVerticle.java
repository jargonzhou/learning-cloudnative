package com.spike.vertx.streams;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A verticle providing a text-based TCP protocol for controlling JukeBox.
 *
 * <p>Use {@link RecordParser} to parse line-based protocol.</p>
 */
public class NetControlVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(NetControlVerticle.class);

  private final String scheduleCommandPrefix = "/schedule ";

  @Override
  public void start() throws Exception {
    vertx.createNetServer()
      .connectHandler(this::handleClient)
      .listen(3000);
  }

  private void handleClient(NetSocket netSocket) {
    RecordParser.newDelimited("\n", netSocket)
      .handler(buffer -> handleBuffer(netSocket, buffer))
      .endHandler(v -> LOG.info("Connection ended"));
  }

  private void handleBuffer(NetSocket netSocket, Buffer buffer) {
    String command = buffer.toString();
    switch (command) {
      case "/list":
        listCommand(netSocket);
        break;
      case "/play":
        vertx.eventBus().send(Destinations.PLAY, "");
        break;
      case "/pause":
        vertx.eventBus().send(Destinations.PAUSE, "");
        break;
      default:
        if (command.startsWith(scheduleCommandPrefix)) {
          schedule(command);
        } else {
          netSocket.write("Unknown command");
        }

    }
  }

  private void listCommand(NetSocket netSocket) {
    vertx.eventBus().request(Destinations.LIST, "", reply -> {
      if (reply.succeeded()) {
        JsonObject payload = (JsonObject) reply.result().body();
        payload.getJsonArray("files").stream()
          .forEach(name -> netSocket.write(name + "\n"));
      } else {
        LOG.error("/list error", reply.cause());
      }
    });
  }

  private void schedule(String command) {
    String track = command.substring(scheduleCommandPrefix.length());
    JsonObject payload = new JsonObject().put("file", track);
    vertx.eventBus().send(Destinations.SCHEDULE, payload);
  }
}
