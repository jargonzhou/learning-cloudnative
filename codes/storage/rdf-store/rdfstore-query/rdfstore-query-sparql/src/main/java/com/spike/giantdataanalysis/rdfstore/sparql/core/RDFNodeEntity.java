package com.spike.giantdataanalysis.rdfstore.sparql.core;

public class RDFNodeEntity {
  private String graph = Constant.DEFAULT_GRAPH;
  private final String node;

  public RDFNodeEntity(String node) {
    this.node = node;
  }

  public RDFNodeEntity(String graph, String node) {
    this.graph = graph;
    this.node = node;
  }

  public String getGraph() {
    return graph;
  }

  public String getNode() {
    return node;
  }

}
