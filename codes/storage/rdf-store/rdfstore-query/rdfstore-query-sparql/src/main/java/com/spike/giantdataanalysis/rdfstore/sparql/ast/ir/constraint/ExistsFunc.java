package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph.GroupGraphPattern;

// [125] ExistsFunc ::= 'EXISTS' GroupGraphPattern
public class ExistsFunc {
  public GroupGraphPattern groupGraphPattern;
}