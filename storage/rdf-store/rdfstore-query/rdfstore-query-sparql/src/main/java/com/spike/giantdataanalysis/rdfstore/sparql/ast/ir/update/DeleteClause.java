package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad.QuadPattern;

// [42] DeleteClause ::= 'DELETE' QuadPattern
public class DeleteClause {
  public QuadPattern quadPattern;
}