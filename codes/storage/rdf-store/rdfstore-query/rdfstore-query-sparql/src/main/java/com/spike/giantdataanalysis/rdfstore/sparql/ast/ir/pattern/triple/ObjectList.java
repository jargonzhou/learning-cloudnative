package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [79] ObjectList ::= Object ( ',' Object )*
public class ObjectList {
  public List<Object> objects = Lists.newArrayList();
}