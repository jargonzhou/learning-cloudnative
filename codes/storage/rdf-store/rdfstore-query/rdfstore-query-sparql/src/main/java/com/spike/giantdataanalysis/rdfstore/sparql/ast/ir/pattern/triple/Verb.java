package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrIri;

// [78] Verb ::= VarOrIri | 'a'
public class Verb {
  public VarOrIri varOrIri;
  public boolean isA = false;
}