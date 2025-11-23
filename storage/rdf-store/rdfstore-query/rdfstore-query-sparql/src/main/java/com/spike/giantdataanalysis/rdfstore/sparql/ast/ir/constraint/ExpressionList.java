package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import java.util.List;

import com.google.common.collect.Lists;

// [72] ExpressionList ::= NIL | '(' Expression ( ',' Expression )* ')'
public class ExpressionList {
  public boolean isNil = false;
  public List<Expression> expressions = Lists.newArrayList();
}