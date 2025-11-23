package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrIri;

// [51] QuadsNotTriples ::= 'GRAPH' VarOrIri '{' TriplesTemplate? '}'
public class QuadsNotTriples {
  public VarOrIri varOrIri;
  public TriplesTemplate triplesTemplate;
}