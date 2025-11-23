package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrTerm;

// [105] GraphNodePath ::= VarOrTerm | TriplesNodePath
public class GraphNodePath {
  public VarOrTerm varOrTerm;
  public TriplesNodePath triplesNodePath;
}