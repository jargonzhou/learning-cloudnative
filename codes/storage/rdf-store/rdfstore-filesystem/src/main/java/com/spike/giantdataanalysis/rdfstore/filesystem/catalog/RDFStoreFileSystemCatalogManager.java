package com.spike.giantdataanalysis.rdfstore.filesystem.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Maps;
import com.google.protobuf.Parser;
import com.spike.giantdataanalysis.rdfstore.commons.lang.IOStreams;
import com.spike.giantdataanalysis.rdfstore.filesystem.configuration.RDFStoreFileSystemConfiguration;
import com.spike.giantdataanalysis.rdfstore.filesystem.exception.RDFStoreFileSystemCatalogException;
import com.spike.giantdataanalysis.rdfstore.metric.RDFStoreMetricRegistry;
import com.spike.giantdataanalysis.rdfstore.metric.key.FileSystemCatalogMetricKeys;

import generated.filesystem.FileStructureProtos.FileCatalog;
import generated.filesystem.FileStructureProtos.FileNo;

/**
 * File System Catalog Manager.
 * <p>
 * WARNING: NOT thread safe.
 */
public final class RDFStoreFileSystemCatalogManager {

  private RDFStoreFileSystemConfiguration configuration;
  private RDFStoreMetricRegistry metricRegistry;

  public RDFStoreFileSystemCatalogManager(RDFStoreFileSystemConfiguration configuration,
      RDFStoreMetricRegistry metricRegistry) {
    this.configuration = configuration;
    this.metricRegistry = metricRegistry;

    Path catalogRoot = Paths.get(configuration.rdfStore.fileSystem.catalogRoot);
    if (!Files.exists(catalogRoot)) {
      try {
        Files.createDirectory(catalogRoot);
      } catch (IOException e) {
        throw RDFStoreFileSystemCatalogException.newE(e);
      }
    }
  }

  public static final String CATALOG_FILE_NO = "fno";
  public static final String CATALOG_FILE_NAME_NO = "name2no";

  // ---------------------------------------------------------------------------
  // properties
  // ---------------------------------------------------------------------------

  /** file no counter. */
  private AtomicLong fileNoCounter = new AtomicLong(0);
  /** file name => file catalog. */
  private ConcurrentMap<String, FileCatalog> fileNameCatalogMap = Maps.newConcurrentMap();

  // ---------------------------------------------------------------------------
  // act as durable
  // ---------------------------------------------------------------------------

  public void restore() {
    Path fno = Paths.get(configuration.rdfStore.fileSystem.catalogRoot, CATALOG_FILE_NO);
    if (Files.exists(fno)) {
      InputStream is = null;
      try {
        is = Files.newInputStream(fno);
        FileNo fileNo = FileNo.parseFrom(is);
        fileNoCounter.set(fileNo.getFileNo());

        this.collectFnoMetrics();
        this.collectFileReadMetrics();
      } catch (IOException e) {
        throw RDFStoreFileSystemCatalogException.newE("initialize from fno failed!", e);
      } finally {
        IOStreams.close(is);
      }
    }

    Path name2no = Paths.get(configuration.rdfStore.fileSystem.catalogRoot, CATALOG_FILE_NAME_NO);
    if (Files.exists(name2no)) {
      InputStream is = null;
      try {
        is = Files.newInputStream(name2no);
        fileNameCatalogMap.clear();

        Parser<FileCatalog> parser = FileCatalog.parser();
        FileCatalog fileCatalog = null;
        while ((fileCatalog = parser.parseDelimitedFrom(is)) != null) {
          fileNameCatalogMap.put(fileCatalog.getFileName(), fileCatalog);
        }

        this.collectFileReadMetrics();
      } catch (IOException e) {
        throw RDFStoreFileSystemCatalogException.newE("initialize from fno failed!", e);
      } finally {
        IOStreams.close(is);
      }
    }

  }

  public void persistent() {
    this.persistentFno();
    this.persistenName2No();
  }

  private void persistentFno() {
    FileNo fileNo = FileNo.newBuilder().setFileNo((int) fileNoCounter.get()).build();
    Path fno = Paths.get(configuration.rdfStore.fileSystem.catalogRoot, CATALOG_FILE_NO);

    OutputStream os = null;
    try {
      os = Files.newOutputStream(fno);
      fileNo.writeTo(os);

      this.collectFileSyncMetrics();
    } catch (IOException e) {
      throw RDFStoreFileSystemCatalogException.newE("persistent fno failed!", e);
    } finally {
      IOStreams.close(os);
    }
  }

  private void persistenName2No() {
    if (fileNameCatalogMap.isEmpty()) {
      return;
    }

    Path name2no = Paths.get(configuration.rdfStore.fileSystem.catalogRoot, CATALOG_FILE_NAME_NO);

    OutputStream os = null;
    try {
      os = Files.newOutputStream(name2no);

      for (String fileName : fileNameCatalogMap.keySet()) {
        FileCatalog.newBuilder(fileNameCatalogMap.get(fileName)).build().writeDelimitedTo(os);
      }

      this.collectFileSyncMetrics();
    } catch (IOException e) {
      throw RDFStoreFileSystemCatalogException.newE("persistent fno failed!", e);
    } finally {
      IOStreams.close(os);
    }
  }

  // ---------------------------------------------------------------------------
  // mapping: file name <=> file no
  // ---------------------------------------------------------------------------

  public FileNo getRegularFileNo(String fileName) {
    FileCatalog fileCatalog = fileNameCatalogMap.get(fileName);
    if (fileCatalog == null) {
      return null;
    }

    return fileCatalog.getFileno();
  }

  // WARNING: O(n)
  public String getRegularFileName(FileNo fileNo) {
    for (FileCatalog fileCatalog : fileNameCatalogMap.values()) {
      if (fileCatalog.getFileno().equals(fileNo)) {
        return fileCatalog.getFileName();
      }
    }

    return null;
  }

  /**
   * @param fileName relative to root
   * @return
   */
  public FileNo createRegularFileNo(String fileName) {
    FileCatalog fileCatalog = fileNameCatalogMap.get(fileName);
    if (fileCatalog != null) {
      return fileCatalog.getFileno();
    }

    Long newFileNo = fileNoCounter.getAndIncrement();
    this.collectFnoMetrics();

    FileNo fileNo = FileNo.newBuilder().setFileNo(newFileNo.intValue()).build();
    fileCatalog = FileCatalog.newBuilder().setFileName(fileName).setFileno(fileNo).build();
    fileNameCatalogMap.put(fileName, fileCatalog);

    this.persistent();

    return fileNo;
  }

  public void deleteRegularFileNo(String fileName) {
    FileCatalog fileCatalog = fileNameCatalogMap.get(fileName);
    if (fileCatalog == null) {
      return;
    }

    fileNameCatalogMap.remove(fileName);

    this.persistent();
  }

  // ---------------------------------------------------------------------------
  // collect metrics
  // ---------------------------------------------------------------------------
  private void collectFileReadMetrics() {
    this.metricRegistry.counter(FileSystemCatalogMetricKeys.FSC_KEY_FILE_READ_COUNTER).inc();
  }

  private void collectFileSyncMetrics() {
    this.metricRegistry.counter(FileSystemCatalogMetricKeys.FSC_KEY_FILE_SYNC_COUNTER).inc();
  }

  private void collectFnoMetrics() {
    this.metricRegistry.gauge(FileSystemCatalogMetricKeys.FSC_KEY_FNO, fileNoCounter.get());
  }

}
