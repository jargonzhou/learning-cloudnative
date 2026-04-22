package com.spike.giantdataanalysis.rdfstore.filesystem.core;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

import generated.filesystem.FileStructureProtos.FileId;
import generated.filesystem.FileStructureProtos.FileNo;

/**
 * File system memory store.
 */
public final class RDFStoreFileSystemMemoryStore {

  // ugly data structure!!!
  // file no => file descriptor
  final ConcurrentMap<FileNo, RDFStoreFileDescriptor> FILEID_MAP = Maps.newConcurrentMap();

  public void initializeFileHandle(String root, String fileName, FileId fileId) {
    RDFStoreFileDescriptor fd = FILEID_MAP.get(fileId.getFileno());
    if (fd == null) {
      fd = new RDFStoreFileDescriptor(root, fileName, fileId);
    }

    FILEID_MAP.put(fileId.getFileno(), fd);
  }

  /**
   * get file handler.
   * @param fileId
   * @return
   */
  public RDFStoreFileDescriptor getFileHandle(FileId fileId) {
    return FILEID_MAP.get(fileId.getFileno());
  }

  public void destroyFileHandle(FileId fileId) {
    FILEID_MAP.remove(fileId.getFileno());
  }
}