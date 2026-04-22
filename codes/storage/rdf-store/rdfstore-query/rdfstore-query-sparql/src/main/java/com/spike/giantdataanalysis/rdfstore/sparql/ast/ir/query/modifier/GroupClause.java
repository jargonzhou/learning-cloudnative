package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier;

import java.util.List;

import com.google.common.collect.Lists;

// [19] GroupClause ::= 'GROUP' 'BY' GroupCondition+
public class GroupClause {
  public List<GroupCondition> gGroupConditions = Lists.newArrayList();
}