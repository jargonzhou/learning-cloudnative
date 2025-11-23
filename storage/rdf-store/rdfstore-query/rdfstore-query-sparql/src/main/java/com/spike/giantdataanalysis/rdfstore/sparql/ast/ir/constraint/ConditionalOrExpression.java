package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import java.util.List;

import com.google.common.collect.Lists;

// [111] ConditionalOrExpression ::= ConditionalAndExpression ( '||' ConditionalAndExpression )*
public class ConditionalOrExpression {
  public List<ConditionalAndExpression> conditionalAndExpressions = Lists.newArrayList();
}