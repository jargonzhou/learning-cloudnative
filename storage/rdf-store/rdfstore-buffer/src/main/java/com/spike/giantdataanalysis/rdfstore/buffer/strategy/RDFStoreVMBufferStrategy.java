package com.spike.giantdataanalysis.rdfstore.buffer.strategy;

import java.nio.ByteBuffer;

import com.spike.giantdataanalysis.rdfstore.filesystem.RDFStoreFileSystem;

import generated.filesystem.BufferStructureProtos.BufferFrame;
import generated.filesystem.FileStructureProtos.PageId;

/**
 * 
 */
public class RDFStoreVMBufferStrategy implements RDFStoreBufferStrategy {

  private ByteBuffer byteBuffer;

  @Override
  public void initializeSpace(int capacityInBytes) {
    byte[] bytes = new byte[capacityInBytes];
    byteBuffer = ByteBuffer.wrap(bytes);
  }

  @Override
  public RDFStoreFileSystem getFS() {
    // TODO Implement RDFStoreBufferStrategy.getFS
    return null;
  }

  @Override
  public boolean isDirty(PageId pageId) {
    // TODO Implement RDFStoreBufferStrategy.isDirty
    return false;
  }

  @Override
  public BufferFrame load(PageId pageId) {
    // TODO Implement RDFStoreBufferStrategy.load
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
