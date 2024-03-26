package com.spike.vertx.streams;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A verticle providing music streaming logic and HTTP interface for music players.
 */
public class JukeBoxVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(JukeBoxVerticle.class);

  private enum State {PLAYING, PAUSED}

  private State currentMode = State.PAUSED;

  private final Queue<String> playlist = new ArrayDeque<>();
  // DEV_ENV: mp3 directory
  private final String mp3Dir = "D:/music";
  private final String downloadPathPrefix = "/download/";

  private final Set<HttpServerResponse> responses = new HashSet<>();

  private AsyncFile currentFile;
  private long positionInFile;

  @Override
  public void start() throws Exception {
    EventBus eventBus = vertx.eventBus();
    eventBus.consumer(Destinations.LIST, this::list);
    eventBus.consumer(Destinations.SCHEDULE, this::schedule);
    eventBus.consumer(Destinations.PLAY, this::play);
    eventBus.consumer(Destinations.PAUSE, this::pause);

    vertx.createHttpServer()
      .requestHandler(this::httpHandler)
      .listen(8080);

    // rate-limit
    vertx.setPeriodic(100, this::streamAudioChunk);
  }

  private void streamAudioChunk(Long timerID) {
    if (currentMode == State.PAUSED) {
      return;
    }
    if (currentFile == null && playlist.isEmpty()) {
      currentMode = State.PAUSED;
      return;
    }
    if (currentFile == null) {
      openNextFile();
    }
    currentFile.read(Buffer.buffer(4096),
      0, positionInFile, 4096, ar -> {
        if (ar.succeeded()) {
          processReadBuffer(ar.result());
        } else {
          LOG.error("Read failed", ar.cause());
          closeCurrentFile();
        }
      });
  }

  private void openNextFile() {
    OpenOptions options = new OpenOptions().setRead(true);
    currentFile = vertx.fileSystem()
      .openBlocking(mp3Dir + "/" + playlist.poll(), options);
    positionInFile = 0;
  }

  private void processReadBuffer(Buffer buffer) {
    positionInFile += buffer.length();
    if (buffer.length() == 0) {
      closeCurrentFile();
      return;
    }
    for (HttpServerResponse response : responses) {
      if (!response.writeQueueFull()) { // back-pressure
        response.write(buffer.copy());
      }
    }
  }

  private void closeCurrentFile() {
    positionInFile = 0;
    currentFile.close();
    currentFile = null;
  }

  private void httpHandler(HttpServerRequest request) {
    if ("/".equals(request.path())) {
      openAudioStream(request);
      return;
    }
    if (request.path().startsWith(downloadPathPrefix)) {
      String path = request.path().substring(downloadPathPrefix.length())
        .replaceAll("/", "");
      download(path, request);
      return;
    }

    request.response().setStatusCode(400).end();
  }

  private void openAudioStream(HttpServerRequest request) {
    HttpServerResponse response = request.response()
      .putHeader("Content-Type", "audio/mpeg")
      .setChunked(true);
    responses.add(response);
    response.endHandler(v -> {
      responses.remove(response);
      LOG.info("A streamer left");
    });
  }

  private void download(String path, HttpServerRequest request) {
    String file = mp3Dir + "/" + path;
    if (!vertx.fileSystem().existsBlocking(file)) {
      request.response().setStatusCode(404).end();
      return;
    }

    OpenOptions options = new OpenOptions().setRead(true);
    vertx.fileSystem().open(file, options, ar -> {
      if (ar.succeeded()) {
        downloadFile(ar.result(), request);
      } else {
        LOG.error("Read failed", ar.cause());
        request.response().setStatusCode(500).end();
      }
    });
  }

  private void downloadFile(AsyncFile file, HttpServerRequest request) {
    HttpServerResponse response = request.response();
    response.setStatusCode(200)
      .putHeader("Content-Type", "audio/mpeg")
      .setChunked(true);

//    file.handler(buffer -> {
//      response.write(buffer);
//      if (response.writeQueueFull()) {
//        file.pause();
//        response.drainHandler(v -> file.resume());
//      }
//    });
//
//    file.endHandler(v -> response.end());

    file.pipeTo(response); // with back-pressure
  }

  private void list(Message<Object> message) {
    vertx.fileSystem().readDir(mp3Dir, ".*mp3$", ar -> {
      if (ar.succeeded()) {
        List<String> files = ar.result()
          .stream()
          .map(File::new)
          .map(File::getName)
          .collect(Collectors.toList());
        JsonObject payload = new JsonObject()
          .put("files", new JsonArray(files));
        message.reply(payload);
      } else {
        LOG.error("readDir failed", ar.cause());
        message.fail(500, ar.cause().getMessage());
      }
    });
  }

  private void schedule(Message<JsonObject> message) {
    String file = message.body().getString("file");
    if (playlist.isEmpty() && currentMode == State.PAUSED) {
      currentMode = State.PLAYING;
    }
    playlist.offer(file);
  }

  private void play(Message<Object> message) {
    this.currentMode = State.PLAYING;
  }

  private void pause(Message<Object> message) {
    this.currentMode = State.PAUSED;
  }
}
