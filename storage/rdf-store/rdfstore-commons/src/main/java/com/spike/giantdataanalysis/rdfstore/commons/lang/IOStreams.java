package com.spike.giantdataanalysis.rdfstore.commons.lang;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 */
public final class IOStreams {

  public static int DEFAULT_RETRY_COUNT = 1;

  public static void close(Closeable closeable) {
    close(closeable, DEFAULT_RETRY_COUNT);
  }

  private static void close(Closeable closeable, int retryCount) {
    if (closeable == null) {
      return;
    }
    if (retryCount < 0) {
      return;
    }

    try {
      closeable.close();
    } catch (IOException e) {
      close(closeable, --retryCount);
    }
  }

}
