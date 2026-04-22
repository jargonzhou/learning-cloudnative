package com.spike.giantdataanalysis.sequences.api.cassandra.support;

import java.io.IOException;

import com.datastax.driver.core.Session;

public interface IWrappedCassandraSession {
  void init() throws IOException;

  Session newSession();

  com.datastax.driver.core.ResultSet execute(String stmt);

  void close() throws IOException;

}
