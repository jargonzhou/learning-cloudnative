package com.spike.giantdataanalysis.rdfstore.metric.key;

/**
 * Metric keys of file system catalog.
 */
public interface FileSystemCatalogMetricKeys {

  String FSC_KEY_FNO = "rs.fsc.fno";

  // ---------------------------------------------------------------------------
  // File read and sync
  // ---------------------------------------------------------------------------
  String FSC_KEY_FILE = "rs.fsc.file";
  String FSC_KEY_FILE_READ_COUNTER = FSC_KEY_FILE + ".read.count";
  String FSC_KEY_FILE_SYNC_COUNTER = FSC_KEY_FILE + ".sync.count";

}
