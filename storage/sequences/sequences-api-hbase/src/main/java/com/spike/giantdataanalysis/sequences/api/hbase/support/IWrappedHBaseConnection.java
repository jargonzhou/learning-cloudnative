package com.spike.giantdataanalysis.sequences.api.hbase.support;

import java.io.IOException;

public interface IWrappedHBaseConnection {

  void init() throws IOException;

  org.apache.hadoop.hbase.client.Table getTable(String tableName);

  void close() throws IOException;
}
