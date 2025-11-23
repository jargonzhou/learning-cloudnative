package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [103] CollectionPath ::= '(' GraphNodePath+ ')'
public class CollectionPath {
  public List<GraphNodePath> graphNodePaths = Lists.newArrayList();
}