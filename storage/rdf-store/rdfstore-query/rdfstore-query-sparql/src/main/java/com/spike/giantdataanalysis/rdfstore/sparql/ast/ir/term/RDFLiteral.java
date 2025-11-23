package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term;

// [129] RDFLiteral ::= String ( LANGTAG | ( '^^' iri ) )?
public class RDFLiteral {
  public String value;
  public String langTag;
  public Iri iri;
}