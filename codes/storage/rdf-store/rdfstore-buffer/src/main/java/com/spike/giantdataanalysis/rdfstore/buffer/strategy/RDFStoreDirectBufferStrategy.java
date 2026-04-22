package com.spike.giantdataanalysis.rdfstore.buffer.strategy;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.protobuf.ByteString;
import com.spike.giantdataanalysis.rdfstore.client.Clients;
import com.spike.giantdataanalysis.rdfstore.filesystem.RDFStoreFileSystem;
import com.spike.giantdataanalysis.rdfstore.filesystem.core.RDFStoreFileDescriptor;

import generated.filesystem.BufferStructureProtos.BufferACB;
import generated.filesystem.BufferStructureProtos.BufferCB;
import generated.filesystem.BufferStructureProtos.BufferFrame;
import generated.filesystem.FileStructureProtos.Block;
import generated.filesystem.FileStructureProtos.BlockId;
import generated.filesystem.FileStructureProtos.FileAccessMode;
import generated.filesystem.FileStructureProtos.FileId;
import generated.filesystem.FileStructureProtos.FileNo;
import generated.filesystem.FileStructureProtos.Page;
import generated.filesystem.FileStructureProtos.PageId;

/**
 * Buffer Strategy based on direct byte buffer.
 */
public class RDFStoreDirectBufferStrategy implements RDFStoreBufferStrategy {

  private final RDFStoreFileSystem fs;
  private final int capacityInBytes;
  private ByteBuffer byteBuffer;

  private ConcurrentLinkedQueue<BufferACB> acbs = Queues.newConcurrentLinkedQueue();
  private ConcurrentMap<Integer, LinkedList<BufferCB>> cbMap = Maps.newConcurrentMap();

  public RDFStoreDirectBufferStrategy(RDFStoreFileSystem fs, int capacityInBytes) {
    this.fs = fs;
    this.capacityInBytes = capacityInBytes;
  }

  public void initializeSpace(int capacityInBytes) {
    byteBuffer = ByteBuffer.allocateDirect(capacityInBytes);

  }

  @Override
  public RDFStoreFileSystem getFS() {
    return fs;
  }

  @Override
  public boolean isDirty(PageId pageId) {
    // TODO Implement RDFStoreBufferStrategy.isDirty
    return false;
  }

  @Override
  public BufferFrame load(PageId pageId) {
    FileNo fileNo = pageId.getFileno();
    String fileName = fs.getCatalogManager().getRegularFileName(fileNo);
    Preconditions.checkState(fileName != null);

    FileId fileId = FileId.newBuilder()//
        .setFileno(fileNo)//
        .build();
    RDFStoreFileDescriptor fd = fs.getMemoryStore().getFileHandle(fileId);
    if (fd == null) {
      fileId = fs.open(fileName, FileAccessMode.READ);
    }

    Block block = fs.read(fileId, BlockId.newBuilder().setBlockId(pageId.getPageno()).build());
    ByteString blockContent = block.getContents();
    Page page = Page.newBuilder().build();

    BufferFrame bufferFrame = BufferFrame.newBuilder()//
        .setPage().build();

    return null;
  }

  @Override
  public void store(PageId pageId) {
    // TODO Implement RDFStoreBufferStrategy.store

  }

  @Override
  public BufferFrame[] findVictims() {
    // TODO Implement RDFStoreBufferStrategy.findVictims
    return null;
  }

  @Override
  public void flushDirtyPages() {
    // TODO Implement RDFStoreBufferStrategy.flushDirtyPages

  }

}
