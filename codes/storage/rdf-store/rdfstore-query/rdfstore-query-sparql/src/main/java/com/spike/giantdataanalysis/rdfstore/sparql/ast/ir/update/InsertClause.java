package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad.QuadPattern;

// [43] InsertClause ::= 'INSERT' QuadPattern
public class InsertClause {
  public QuadPattern quadPattern;
}