package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment.Bind;

// [56] GraphPatternNotTriples ::= GroupOrUnionGraphPattern | OptionalGraphPattern
// | MinusGraphPattern | GraphGraphPattern | ServiceGraphPattern | Filter | Bind | InlineData
public class GraphPatternNotTriples {
  public enum GraphPatternNotTriplesTypeEnum {
    GroupOrUnionGraphPattern, OptionalGraphPattern, MinusGraphPattern, GraphGraphPattern,
    ServiceGraphPattern, Filter, Bind, InlineData
  }

  public GraphPatternNotTriplesTypeEnum type;

  public GroupOrUnionGraphPattern groupOrUnionGraphPattern;
  public OptionalGraphPattern optionalGraphPattern;
  public MinusGraphPattern minusGraphPattern;
  public GraphGraphPattern graphGraphPattern;
  public ServiceGraphPattern serviceGraphPattern;
  public Filter filter;
  public Bind bind;
  public InlineData inlineData;
}