package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier;

import java.util.List;

import com.google.common.collect.Lists;

// [21] HavingClause ::= 'HAVING' HavingCondition+
public class HavingClause {
  public List<HavingCondition> gHavingConditions = Lists.newArrayList();
}