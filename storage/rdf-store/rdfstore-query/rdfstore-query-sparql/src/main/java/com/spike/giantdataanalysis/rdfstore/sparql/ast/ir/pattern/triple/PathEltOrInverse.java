package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

// [92] PathEltOrInverse ::= PathElt | '^' PathElt
public class PathEltOrInverse {
  public boolean isInverse = false;
  public PathElt pathElt;
}