package com.spike.giantdataanalysis.rdfstore.filesystem;

import com.spike.giantdataanalysis.rdfstore.filesystem.catalog.RDFStoreFileSystemCatalogManager;
import com.spike.giantdataanalysis.rdfstore.filesystem.core.RDFStoreFileSystemMemoryStore;

import generated.filesystem.FileStructureProtos.Block;
import generated.filesystem.FileStructureProtos.BlockId;
import generated.filesystem.FileStructureProtos.FileAccessMode;
import generated.filesystem.FileStructureProtos.FileId;

/**
 * File System Abstraction.
 * <p>
 * {@link FileId} represent as a file handle, never change.
 */
public interface RDFStoreFileSystem {

  long getUsableSpaceBytes();

  RDFStoreFileSystemCatalogManager getCatalogManager();

  RDFStoreFileSystemMemoryStore getMemoryStore();

  /**
   * create and allocate file.
   */
  void create(String fileName);

  /**
   * delete and deallocate file.
   */
  void delete(String fileName);

  /**
   * open a file with access mode.
   * @return
   */
  FileId open(String fileName, FileAccessMode accessMode);

  /**
   * close a file.
   */
  void close(FileId fileId);

  /**
   * extend an existed file with give parameter.
   */
  void extend(FileId fileId);

  /**
   * read entire block in disk to buffer in memory.
   * @return
   */
  Block read(FileId fileId, BlockId blockId);

  /**
   * read <code>blockCount</code> blocks in disk to buffer in memory.
   */
  Block[] readc(FileId fileId, BlockId startBlockId, int blockCount);

  /**
   * write from buffer in memory to block in disk.
   */
  void write(FileId fileId, Block block);

  /**
   * write continuously from buffer in memory to block in disk.
   */
  void writec(FileId fileId, BlockId startBlockId, Block[] blocks);

  /**
   * flush to disk.
   */
  void flush(FileId fileId);

}