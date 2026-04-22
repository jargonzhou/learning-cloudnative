package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;

// [128] iriOrFunction ::= iri ArgList?
public class IriOrFunction {
  public Iri iri;
  public ArgList argList;
}