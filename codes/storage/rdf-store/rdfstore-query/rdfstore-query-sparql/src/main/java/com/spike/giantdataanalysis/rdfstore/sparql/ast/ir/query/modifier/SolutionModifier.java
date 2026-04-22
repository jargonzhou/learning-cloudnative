package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier;

// [18] SolutionModifier ::= GroupClause? HavingClause? OrderClause? LimitOffsetClauses?
public class SolutionModifier {
  public GroupClause groupClause;
  public HavingClause havingClause;
  public OrderClause orderClause;
  public LimitOffsetClauses limitOffsetClauses;
}