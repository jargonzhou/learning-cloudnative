package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import java.util.List;

import com.google.common.collect.Lists;

// [71] ArgList ::= NIL | '(' 'DISTINCT'? Expression ( ',' Expression )* ')'
public class ArgList {
  public boolean isNil = false;

  public boolean distinct;
  public List<Expression> expressions = Lists.newArrayList();
}