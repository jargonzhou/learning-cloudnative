package com.spike.giantdataanalysis.sequences.api.hbase.support;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseWrappedConnection implements IWrappedHBaseConnection {
  private static final Logger LOG = LoggerFactory.getLogger(HBaseWrappedConnection.class);

  private org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
  private Connection connection;

  public HBaseWrappedConnection() {
  }

  public HBaseWrappedConnection(org.apache.hadoop.conf.Configuration conf) {
    this.conf = conf;
  }

  @Override
  public void init() throws IOException {
    // 从类路径加载资源
    conf.addResource("conf/hbase-site.xml");
    connection = ConnectionFactory.createConnection(conf);
  }

  @Override
  public Table getTable(String tableName) {
    try {
      return connection.getTable(TableName.valueOf(tableName));
    } catch (IOException e) {
      LOG.error("", e);
      return null;
    }
  }

  @Override
  public void close() throws IOException {
    if (connection != null) connection.close();
  }

}
