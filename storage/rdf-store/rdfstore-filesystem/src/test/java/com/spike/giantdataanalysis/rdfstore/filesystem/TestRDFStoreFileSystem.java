package com.spike.giantdataanalysis.rdfstore.filesystem;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.protobuf.ByteString;
import com.spike.giantdataanalysis.rdfstore.filesystem.configuration.RDFStoreFileSystemConfiguration;
import com.spike.giantdataanalysis.rdfstore.filesystem.exception.RDFStoreFileSystemException;
import com.spike.giantdataanalysis.rdfstore.metric.RDFStoreMetricRegistry;

import generated.filesystem.FileStructureProtos.Block;
import generated.filesystem.FileStructureProtos.BlockHeader;
import generated.filesystem.FileStructureProtos.BlockId;
import generated.filesystem.FileStructureProtos.FileAccessMode;
import generated.filesystem.FileStructureProtos.FileId;

public class TestRDFStoreFileSystem {

  private static RDFStoreFileSystemConfiguration configuration;
  private static RDFStoreMetricRegistry metricRegistry;
  private static RDFStoreFileSystem fs;

  @BeforeClass
  public static void setUpBeforeClass() {
    configuration = RDFStoreFileSystemConfiguration.configuration;
    metricRegistry = new RDFStoreMetricRegistry();
    fs = new DefaultRDFStoreFileSystem(configuration, metricRegistry);
    Assert.assertNotNull(fs);

    // start JMX reporter
    metricRegistry.initializeJmxReporter();
    metricRegistry.startJmxReporter();
  }

  @Test
  public void testCreate() {
    final String fileName = "graph1.db";

    // delete
    fs.delete(fileName);

    // create
    fs.create(fileName);

    // open
    fs.open(fileName, FileAccessMode.WRITE);

    // delete
    fs.delete(fileName);
  }

  @Test(expected = RDFStoreFileSystemException.class)
  public void testClose() {
    final String fileName = "graph1.db";

    // open
    FileId fileId = fs.open(fileName, FileAccessMode.WRITE);

    // close
    fs.close(fileId);

    // read after close
    BlockId blockId = BlockId.newBuilder()//
        .setBlockId(2).build();
    try {
      fs.read(fileId, blockId);
    } catch (RDFStoreFileSystemException e) {
      System.out.println("this error should happen!");
    }
  }

  @Test
  public void integrationTest() {
    final String fileName = "graph1.db";

    // force delete
    fs.delete(fileName);

    // open
    FileId fileId = fs.open(fileName, FileAccessMode.WRITE);

    // write
    BlockId blockId = BlockId.newBuilder()//
        .setBlockId(0).build();
    BlockHeader blockHeader = BlockHeader.newBuilder()//
        .setFlip(true)//
        .setFileno(fileId.getFileno())//
        .setBlockno(blockId).build();
    byte[] bytes = new byte[100];
    ByteString contents = ByteString.copyFrom(bytes);

    Block block = Block.newBuilder()//
        .setHeader(blockHeader)//
        .setContents(contents)//
        .setFlop(true).build();
    fs.write(fileId, block);

    // writec
    Block[] blocks = new Block[3];
    for (int i = 1, len = blocks.length; i <= len; i++) {
      BlockId _blockId = BlockId.newBuilder()//
          .setBlockId(i).build();
      BlockHeader _blockHeader = BlockHeader.newBuilder()//
          .setFlip(true)//
          .setFileno(fileId.getFileno())//
          .setBlockno(_blockId).build();
      byte[] _bytes = new byte[100];
      ByteString _contents = ByteString.copyFrom(_bytes);
      blocks[i - 1] = Block.newBuilder()//
          .setHeader(_blockHeader)//
          .setContents(_contents)//
          .setFlop(true).build();
    }

    BlockId startBlockId = BlockId.newBuilder()//
        .setBlockId(1).build();
    fs.writec(fileId, startBlockId, blocks);

    // readc
    startBlockId = BlockId.newBuilder()//
        .setBlockId(0).build();
    Block[] returnBlocks = fs.readc(fileId, startBlockId, 2);
    Assert.assertNotNull(returnBlocks);
    Assert.assertEquals(2, returnBlocks.length);
    for (Block returnBlock : returnBlocks) {
      System.out.println("readc: " + returnBlock);
    }

    // read
    startBlockId = BlockId.newBuilder()//
        .setBlockId(2).build();
    Block returnBlock = fs.read(fileId, startBlockId);
    Assert.assertNotNull(returnBlock);
    System.out.println("readc: " + returnBlock);

    // delete
    fs.delete(fileName);

    // for view metrics
    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
