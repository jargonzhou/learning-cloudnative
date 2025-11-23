package com.spike.giantdataanalysis.rdfstore.metric.key;

/**
 * Metric keys of buffer.
 */
public interface BufferMetricKeys {
  // ---------------------------------------------------------------------------
  // Memory: utilization
  // ---------------------------------------------------------------------------
  String BUF_KEY_MEM = "rs.buf.mem";

  // ---------------------------------------------------------------------------
  // Cache: hit, miss
  // ---------------------------------------------------------------------------
  String BUF_KEY_HIT_COUNTER = "rs.buf.hit";
  String BUF_KEY_MISS_COUNTER = "rs.buf.miss";

}
