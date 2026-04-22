package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.NumericLiteral;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.RDFLiteral;

// [65] DataBlockValue ::= iri | RDFLiteral | NumericLiteral | BooleanLiteral | 'UNDEF'
public class DataBlockValue {
  public Iri giri;
  public RDFLiteral rDFLiteral;
  public NumericLiteral numericLiteral;
  public Boolean booleanLiteral;
  public boolean isUndef = false;
}