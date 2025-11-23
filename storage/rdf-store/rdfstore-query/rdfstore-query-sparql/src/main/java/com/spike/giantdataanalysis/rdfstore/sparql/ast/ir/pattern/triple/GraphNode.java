package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrTerm;

// [104] GraphNode ::= VarOrTerm | TriplesNode
public class GraphNode {
  public VarOrTerm varOrTerm;
  public TriplesNode triplesNode;
}