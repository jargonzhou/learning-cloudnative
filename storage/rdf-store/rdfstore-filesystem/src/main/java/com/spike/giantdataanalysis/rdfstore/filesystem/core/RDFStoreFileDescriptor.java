package com.spike.giantdataanalysis.rdfstore.filesystem.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

import com.spike.giantdataanalysis.rdfstore.filesystem.exception.RDFStoreFileSystemException;

import generated.filesystem.FileStructureProtos.FileAccessMode;
import generated.filesystem.FileStructureProtos.FileId;

/**
 * File handle: always readable and writable.
 */
public class RDFStoreFileDescriptor {

  public FileId fileId;
  public String fileName;
  public RandomAccessFile raf; // opened file handle in java

  public RDFStoreFileDescriptor(String root, String fileName, FileId fileId) {
    this.fileName = fileName;
    this.fileId = fileId;
    FileAccessMode fam = fileId.getAccessMode();
    String rafMode = null;
    switch (fam) {
    case READ:
    case APPEND:
    case WRITE:
      rafMode = "rw";
      break;
    default:
      throw RDFStoreFileSystemException.newE("unsupported accessmode: " + fam + "!");
    }

    try {
      this.raf = new RandomAccessFile(Paths.get(root, fileName).toFile(), rafMode);
      if (fam.equals(FileAccessMode.APPEND)) {
        this.raf.seek(this.raf.length());
      }
    } catch (FileNotFoundException e) {
      throw RDFStoreFileSystemException.newE(e);
    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE(e);
    }
  }
}