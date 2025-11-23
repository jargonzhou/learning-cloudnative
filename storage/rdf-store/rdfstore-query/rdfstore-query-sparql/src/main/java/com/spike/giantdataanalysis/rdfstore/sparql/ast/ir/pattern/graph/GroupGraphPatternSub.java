package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph;

import java.util.List;

import com.google.common.collect.Lists;

// [54] GroupGraphPatternSub ::= TriplesBlock? ( GraphPatternNotTriples '.'? TriplesBlock? )*
public class GroupGraphPatternSub {
  public class GraphPatternNotTriplesAndTriplesBlock {
    public GraphPatternNotTriples graphPatternNotTriples;
    public TriplesBlock triplesBlock;
  }

  public TriplesBlock triplesBlock;
  public List<GraphPatternNotTriplesAndTriplesBlock> list = Lists.newArrayList();
}