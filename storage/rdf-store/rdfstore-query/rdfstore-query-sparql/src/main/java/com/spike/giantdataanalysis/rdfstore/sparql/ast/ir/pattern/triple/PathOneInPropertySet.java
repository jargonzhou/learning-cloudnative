package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;

// [96] PathOneInPropertySet ::= iri | 'a' | '^' ( iri | 'a' )
public class PathOneInPropertySet {
  public Iri iri;
  public boolean isA = false;
  public boolean isInverse = false;
}