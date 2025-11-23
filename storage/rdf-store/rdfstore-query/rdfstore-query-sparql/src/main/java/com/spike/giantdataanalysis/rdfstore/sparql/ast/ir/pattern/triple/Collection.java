package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [102] Collection ::= '(' GraphNode+ ')'
public class Collection {
  public List<GraphNode> graphNodes = Lists.newArrayList();
}