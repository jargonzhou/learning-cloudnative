package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Var;

// [63] InlineDataOneVar ::= Var '{' DataBlockValue* '}'
public class InlineDataOneVar {
  public Var var;
  public List<DataBlockValue> dataBlockValues = Lists.newArrayList();
}