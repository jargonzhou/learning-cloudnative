package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier;

// [25] LimitOffsetClauses ::= LimitClause OffsetClause? | OffsetClause LimitClause?
public class LimitOffsetClauses {
  // do not care the order
  public LimitClause limitClause;
  public OffsetClause offsetClause;
}