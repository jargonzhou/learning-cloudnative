package com.spike.giantdataanalysis.sequences.api.cassandra.support;

public final class DatastaxCassandraSessionConfiguration {

  private String clusterName = "Test Cluster";
  private String contactPointHost = "127.0.0.1";
  private String keyspace = "playground";

  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public String getContactPointHost() {
    return contactPointHost;
  }

  public void setContactPointHost(String contactPointHost) {
    this.contactPointHost = contactPointHost;
  }

  public String getKeyspace() {
    return keyspace;
  }

  public void setKeyspace(String keyspace) {
    this.keyspace = keyspace;
  }

}
