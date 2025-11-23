package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrTerm;

// [81] TriplesSameSubjectPath ::= VarOrTerm PropertyListPathNotEmpty
// | TriplesNodePath PropertyListPath
public class TriplesSameSubjectPath {
  // branch 1
  public VarOrTerm varOrTerm;
  public PropertyListPathNotEmpty propertyListPathNotEmpty;
  
  // branch 2
  public TriplesNodePath triplesNodePath;
  public PropertyListPath propertyListPath;
}