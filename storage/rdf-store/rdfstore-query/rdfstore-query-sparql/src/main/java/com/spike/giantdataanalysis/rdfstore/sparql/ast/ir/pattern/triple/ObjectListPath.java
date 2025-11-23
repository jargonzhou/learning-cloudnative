package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [86] ObjectListPath ::= ObjectPath ( ',' ObjectPath )*
public class ObjectListPath {
  public List<ObjectPath> objectPaths = Lists.newArrayList();
}