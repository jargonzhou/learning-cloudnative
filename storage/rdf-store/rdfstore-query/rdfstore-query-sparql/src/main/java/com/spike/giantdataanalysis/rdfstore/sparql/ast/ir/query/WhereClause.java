package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph.GroupGraphPattern;

// [17] WhereClause ::= 'WHERE'? GroupGraphPattern
public class WhereClause {
  public GroupGraphPattern groupGraphPattern;
}