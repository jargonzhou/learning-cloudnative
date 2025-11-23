package com.spike.giantdataanalysis.rdfstore.sparql.core;

public final class RDFStatement {
  private String graph = Constant.DEFAULT_GRAPH;
  private final String subject;
  private final String predicate;
  private final String object;

  public RDFStatement(String subject, String predicate, String object) {
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
  }

  public RDFStatement(String graph, String subject, String predicate, String object) {
    this.graph = graph;
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
  }

  public String getGraph() {
    return graph;
  }

  public String getSubject() {
    return subject;
  }

  public String getPredicate() {
    return predicate;
  }

  public String getObject() {
    return object;
  }

}
