package com.spike.giantdataanalysis.sequences.api.cassandra.support;

import java.io.IOException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public final class CassandraWrappedSession implements IWrappedCassandraSession {

  private final DatastaxCassandraSessionConfiguration configuration;
  private Cluster cluster;
  private Session session;

  public CassandraWrappedSession(DatastaxCassandraSessionConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public ResultSet execute(String stmt) {
    return session.execute(stmt);
  }

  @Override
  public void init() throws IOException {
    cluster = Cluster.builder()//
        .withClusterName(configuration.getClusterName())// client side cluster name
        .addContactPoint(configuration.getContactPointHost()).build();

    final String keyspace = configuration.getKeyspace();
    this.session = cluster.connect(keyspace);
  }

  @Override
  public Session newSession() {
    Session session = cluster.connect(configuration.getKeyspace());
    this.session = session;
    return this.session;
  }

  @Override
  public void close() throws IOException {
    if (session != null) session.close();
    if (cluster != null) cluster.close();
  }

}
