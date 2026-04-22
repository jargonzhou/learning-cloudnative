package com.spike.giantdataanalysis.rdfstore.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.spike.giantdataanalysis.rdfstore.commons.lang.IOStreams;

import generated.filesystem.FileStructureProtos.DataFileHeader;

/**
 * 
 */
public class RDFStoreFileSystemProtoBufs {

  public static void writeDelimitedTo(com.google.protobuf.GeneratedMessageV3 message,
      Path filePath) {
    OutputStream os = null;

    try {
      os = Files.newOutputStream(filePath);
      message.writeDelimitedTo(os);
    } catch (IOException e) {
      return;
    } finally {
      IOStreams.close(os);
    }
  }

  public static DataFileHeader readDataFileHeader(Path filePath) {
    InputStream is = null;

    try {
      is = Files.newInputStream(filePath);
      return DataFileHeader.parseDelimitedFrom(is);
    } catch (IOException e) {
      return null;
    } finally {
      IOStreams.close(is);
    }
  }
}
