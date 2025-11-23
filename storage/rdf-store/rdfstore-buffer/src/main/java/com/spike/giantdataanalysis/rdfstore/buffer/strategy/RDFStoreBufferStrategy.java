package com.spike.giantdataanalysis.rdfstore.buffer.strategy;

import com.spike.giantdataanalysis.rdfstore.filesystem.RDFStoreFileSystem;

import generated.filesystem.BufferStructureProtos.BufferFrame;
import generated.filesystem.FileStructureProtos.PageId;

/**
 * Buffer Strategy.
 */
public interface RDFStoreBufferStrategy {
  enum BufferStrategyEnum {

    DIRECT("direct"), //
    VM("vm");

    private String key;

    BufferStrategyEnum(String key) {
      this.key = key;
    }

    public static BufferStrategyEnum get(String key) {
      for (BufferStrategyEnum e : BufferStrategyEnum.values()) {
        if (e.key.equals(key)) {
          return e;
        }
      }

      return BufferStrategyEnum.DIRECT;
    }
  }

  void initializeSpace(int capacityInBytes);

  RDFStoreFileSystem getFS();

  boolean isDirty(PageId pageId);

  BufferFrame load(PageId pageId);

  void store(PageId pageId);

  BufferFrame[] findVictims();

  void flushDirtyPages();

}
