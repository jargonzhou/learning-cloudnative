package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;

// [44] UsingClause ::= 'USING' ( iri | 'NAMED' iri )
public class UsingClause {
  public boolean isNamed = false;
  public Iri iri;
}