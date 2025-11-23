package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph.GroupGraphPattern;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;

// [41] Modify ::= ( 'WITH' iri )? ( DeleteClause InsertClause? | InsertClause ) UsingClause*
// 'WHERE' GroupGraphPattern
public class Modify {
  public Iri iri;
  public DeleteClause deleteClause;
  public InsertClause insertClause;
  public List<UsingClause> usingClauses = Lists.newArrayList();
  public GroupGraphPattern groupGraphPattern;
}