package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrTerm;

// [75] TriplesSameSubject ::= VarOrTerm PropertyListNotEmpty | TriplesNode PropertyList
public class TriplesSameSubject {
  // branch 1
  public VarOrTerm varOrTerm;
  public PropertyListNotEmpty propertyListNotEmpty;

  // branch 2
  public TriplesNode triplesNode;
  public PropertyList propertyList;
}