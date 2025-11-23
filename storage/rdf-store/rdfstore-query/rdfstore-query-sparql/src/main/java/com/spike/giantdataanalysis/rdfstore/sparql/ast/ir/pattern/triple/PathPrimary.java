package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;

// [94] PathPrimary ::= iri | 'a' | '!' PathNegatedPropertySet | '(' Path ')'
public class PathPrimary {
  public Iri iri;
  public boolean isA = false;
  public PathNegatedPropertySet pathNegatedPropertySet;
  public Path path;
}