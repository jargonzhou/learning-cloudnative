package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;

// [70] FunctionCall ::= iri ArgList
public class FunctionCall {
  public Iri iri;
  public ArgList argList;
}