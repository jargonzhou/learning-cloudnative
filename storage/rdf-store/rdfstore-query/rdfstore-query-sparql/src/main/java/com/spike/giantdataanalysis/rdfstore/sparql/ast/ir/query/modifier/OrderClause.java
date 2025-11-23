package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier;

import java.util.List;

import com.google.common.collect.Lists;

// [23] OrderClause ::= 'ORDER' 'BY' OrderCondition+
public class OrderClause {
  public List<OrderCondition> orderConditions = Lists.newArrayList();
}