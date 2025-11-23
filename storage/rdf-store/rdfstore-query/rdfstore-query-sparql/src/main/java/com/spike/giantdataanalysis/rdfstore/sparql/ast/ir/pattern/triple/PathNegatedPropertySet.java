package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [95] PathNegatedPropertySet ::= PathOneInPropertySet |
// '(' ( PathOneInPropertySet ( '|' PathOneInPropertySet )* )? ')'
public class PathNegatedPropertySet {
  public PathOneInPropertySet pathOneInPropertySet1;
  public PathOneInPropertySet pathOneInPropertySet2;
  public List<PathOneInPropertySet> pathOneInPropertySets = Lists.newArrayList();
}