package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import java.util.List;

import com.google.common.collect.Lists;

// [112] ConditionalAndExpression ::= ValueLogical ( '&&' ValueLogical )*
public class ConditionalAndExpression {
  public List<ValueLogical> valueLogicals = Lists.newArrayList();
}