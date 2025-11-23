package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph;

import java.util.List;

import com.google.common.collect.Lists;

// [67] GroupOrUnionGraphPattern ::= GroupGraphPattern ( 'UNION' GroupGraphPattern )*
public class GroupOrUnionGraphPattern {
  public List<GroupGraphPattern> groupGraphPatterns = Lists.newArrayList();
}