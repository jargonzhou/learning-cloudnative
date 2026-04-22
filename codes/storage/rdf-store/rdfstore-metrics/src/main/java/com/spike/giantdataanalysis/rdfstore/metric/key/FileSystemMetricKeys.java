package com.spike.giantdataanalysis.rdfstore.metric.key;

/**
 * Metric keys of file system.
 */
public interface FileSystemMetricKeys {

  // ---------------------------------------------------------------------------
  // Memory
  // ---------------------------------------------------------------------------
  String FS_KEY_MEM = "rs.fs.mem";

  String FS_KEY_MEM_ALLOCATE_METER = FS_KEY_MEM + ".allo.rate";
  String FS_KEY_MEM_ALLOCATE_HISTOGRAM = FS_KEY_MEM + ".allo.dist";

  // ---------------------------------------------------------------------------
  // Random Access File
  // ---------------------------------------------------------------------------
  String FS_KEY_RAF = "rs.fs.raf";
  String FS_KEY_RAF_SEEK_COUNTER = FS_KEY_RAF + ".seek.count";
  String FS_KEY_RAF_SEEK_SKIP_HISTOGRAM = FS_KEY_RAF + ".seek.dist";

}
