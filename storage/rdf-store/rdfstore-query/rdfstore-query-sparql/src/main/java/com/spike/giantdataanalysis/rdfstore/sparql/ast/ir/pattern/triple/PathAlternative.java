package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [89] PathAlternative ::= PathSequence ( '|' PathSequence )*
public class PathAlternative {
  public List<PathSequence> pathSequences = Lists.newArrayList();
}