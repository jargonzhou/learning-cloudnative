package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad.QuadPattern;

// [40] DeleteWhere ::= 'DELETE WHERE' QuadPattern
public class DeleteWhere {
  public QuadPattern quadPattern;
}