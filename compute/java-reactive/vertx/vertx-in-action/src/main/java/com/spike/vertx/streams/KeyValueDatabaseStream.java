package com.spike.vertx.streams;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A key-value database file stream reader and writer.
 */
public class KeyValueDatabaseStream {
  private static final Logger LOG = LoggerFactory.getLogger(KeyValueDatabaseStream.class);

  private final String dbPath;

  private static final byte[] MAGIC_HEADER = new byte[] {1,2,3,4};
  private static final int INT_BYTE_SIZE = Integer.SIZE / Byte.SIZE;

  /**
   * File format.
   * <pre>
   * +---------------+
   * |Magic Header   | 1 2 3 4
   * +---------------+
   * |Version        | integer
   * +---------------+
   * |Name           | database name, end with newline
   * +---------------+
   * |Key Length     | integer
   * +---------------+
   * |Key Name       | characters
   * +---------------+
   * |Value Length   | integer
   * +---------------+
   * |Value          | characters
   * +---------------+
   * |(...)          | key-value sequences
   * +---------------+
   * </pre>
   */
  public KeyValueDatabaseStream(String dbPath) {
    this.dbPath = dbPath;
  }

  public void write(Vertx vertx) {
    AsyncFile dbFile = vertx.fileSystem().openBlocking(dbPath,
      new OpenOptions().setWrite(true).setCreate(true));

    Buffer buffer = Buffer.buffer();
    buffer.appendBytes(MAGIC_HEADER);
    buffer.appendInt(2);
    buffer.appendString("Sample database\n");
    String key = "abc";
    String value = "123456-abcdef";
    buffer.appendInt(key.length())
      .appendString(key)
      .appendInt(value.length())
      .appendString(value);
    key = "foo@bar";
    value = "For Bar Baz";
    buffer.appendInt(key.length())
      .appendString(key)
      .appendInt(value.length())
      .appendString(value);

    dbFile.end(buffer, ar -> vertx.close());
  }

  public void read(Vertx vertx) {
    AsyncFile dbFile = vertx.fileSystem().openBlocking(dbPath,
      new OpenOptions().setRead(true));

    // parser mode: fixed length
    RecordParser parser = RecordParser.newFixed(MAGIC_HEADER.length, dbFile);
    parser.handler(magicHeader -> readMagicHeader(parser, magicHeader));
  }

  private void readMagicHeader(RecordParser parser, Buffer magicHeader) {
    LOG.info("Magic header: {}:{}:{}:{}", magicHeader.getByte(0),
      magicHeader.getByte(1),
      magicHeader.getByte(2),
      magicHeader.getByte(3));
    parser.handler(version -> readVersion(parser, version));
  }

  private void readVersion(RecordParser parser, Buffer version) {
    LOG.info("Version: {}", version.getInt(0));
    // parser mode: newline separated
    parser.delimitedMode("\n");
    parser.handler(name -> readName(parser, name));
  }

  private void readName(RecordParser parser, Buffer name) {
    LOG.info("Name: {}", name.toString());
    // parser mode: fixed length
    parser.fixedSizeMode(INT_BYTE_SIZE);
    parser.handler(keyLength -> readKey(parser, keyLength));
  }

  private void readKey(RecordParser parser, Buffer keyLength) {
    // parse mode: fixed length
    parser.fixedSizeMode(keyLength.getInt(0));
    parser.handler(key -> readValue(parser, key));
  }

  private void readValue(RecordParser parser, Buffer key) {
    // parse mode: fixed length
    parser.fixedSizeMode(INT_BYTE_SIZE);
    parser.handler(valueLength -> readEntry(parser, key, valueLength));
  }

  private void readEntry(RecordParser parser, Buffer key, Buffer valueLength) {
    // parse mode: fixed length
    parser.fixedSizeMode(valueLength.getInt(0));
    parser.handler(value -> {
      LOG.info("Key: {} / Value: {}", key.toString(), value);

      // parse mode: fixed length
      parser.fixedSizeMode(INT_BYTE_SIZE);
      parser.handler(keyLength -> readKey(parser, keyLength));
    });
  }

  // consumer pull stream to read.
  public void readPull(Vertx vertx) {
    AsyncFile dbFile = vertx.fileSystem().openBlocking(dbPath,
      new OpenOptions().setRead(true));

    // parser mode: fixed length
    RecordParser parser = RecordParser.newFixed(MAGIC_HEADER.length, dbFile);
    parser.pause();
    parser.fetch(1);
    parser.handler(magicHeader -> readMagicHeaderPull(parser, magicHeader));
  }

  private void readMagicHeaderPull(RecordParser parser, Buffer magicHeader) {
    LOG.info("Magic header: {}:{}:{}:{}", magicHeader.getByte(0),
      magicHeader.getByte(1),
      magicHeader.getByte(2),
      magicHeader.getByte(3));
    parser.handler(version -> readVersionPull(parser, version));
    parser.fetch(1);
  }

  private void readVersionPull(RecordParser parser, Buffer version) {
    LOG.info("Version: {}", version.getInt(0));
    // parser mode: newline separated
    parser.delimitedMode("\n");
    parser.handler(name -> readNamePull(parser, name));
    parser.fetch(1);
  }

  private void readNamePull(RecordParser parser, Buffer name) {
    LOG.info("Name: {}", name.toString());
    // parser mode: fixed length
    parser.fixedSizeMode(INT_BYTE_SIZE);
    parser.handler(keyLength -> readKeyPull(parser, keyLength));
    parser.fetch(1);
  }

  private void readKeyPull(RecordParser parser, Buffer keyLength) {
    // parse mode: fixed length
    parser.fixedSizeMode(keyLength.getInt(0));
    parser.handler(key -> readValuePull(parser, key));
    parser.fetch(1);
  }

  private void readValuePull(RecordParser parser, Buffer key) {
    // parse mode: fixed length
    parser.fixedSizeMode(INT_BYTE_SIZE);
    parser.handler(valueLength -> readEntryPull(parser, key, valueLength));
    parser.fetch(1);
  }

  private void readEntryPull(RecordParser parser, Buffer key, Buffer valueLength) {
    // parse mode: fixed length
    parser.fixedSizeMode(valueLength.getInt(0));
    parser.handler(value -> {
      LOG.info("Key: {} / Value: {}", key.toString(), value);

      // parse mode: fixed length
      parser.fixedSizeMode(INT_BYTE_SIZE);
      parser.handler(keyLength -> readKeyPull(parser, keyLength));
      parser.fetch(1);
    });
    parser.fetch(1);
  }
}
