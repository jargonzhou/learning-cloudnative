package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph.GroupGraphPattern;

// [126] NotExistsFunc ::= 'NOT' 'EXISTS' GroupGraphPattern
public class NotExistsFunc {
  public GroupGraphPattern groupGraphPattern;
}