package com.spike.giantdataanalysis.rdfstore.sparql.core;

public final class RDFNodeId {
  private String node;
  private byte[] id;

  private RDFNodeId() {
  }

  public String getNode() {
    return node;
  }

  public byte[] getId() {
    return id;
  }

}
