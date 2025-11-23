package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrIri;

// [58] GraphGraphPattern ::= 'GRAPH' VarOrIri GroupGraphPattern
public class GraphGraphPattern {
  public VarOrIri varOrIri;
  public GroupGraphPattern groupGraphPattern;
}