package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.SubSelect;

// [53] GroupGraphPattern ::= '{' ( SubSelect | GroupGraphPatternSub ) '}'
public class GroupGraphPattern {
  public SubSelect subSelect;
  public GroupGraphPatternSub groupGraphPatternSub;
}