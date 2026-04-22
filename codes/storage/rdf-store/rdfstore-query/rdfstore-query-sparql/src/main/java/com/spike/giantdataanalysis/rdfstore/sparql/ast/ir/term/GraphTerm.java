package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term;

// [109] GraphTerm ::= iri | RDFLiteral | NumericLiteral | BooleanLiteral | BlankNode | NIL
public class GraphTerm {
  public Iri iri;
  public RDFLiteral rDFLiteral;
  public NumericLiteral numericLiteral;
  public Boolean booleanLiteral;
  public BlankNode blankNode;
  public boolean isNil = false;
}