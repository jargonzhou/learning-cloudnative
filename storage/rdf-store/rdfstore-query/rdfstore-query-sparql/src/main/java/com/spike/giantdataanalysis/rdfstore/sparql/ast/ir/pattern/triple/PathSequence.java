package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [90] PathSequence ::= PathEltOrInverse ( '/' PathEltOrInverse )*
public class PathSequence {
  public List<PathEltOrInverse> pathEltOrInverses = Lists.newArrayList();
}