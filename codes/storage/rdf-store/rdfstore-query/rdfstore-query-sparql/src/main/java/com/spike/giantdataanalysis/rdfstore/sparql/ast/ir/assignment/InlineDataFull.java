package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Var;

// [64] InlineDataFull ::= ( NIL | '(' Var* ')' ) '{' ( '(' DataBlockValue* ')' | NIL )* '}'
public class InlineDataFull {
  public class DataBlockValueOrNIL {
    public List<DataBlockValue> dataBlockValue = Lists.newArrayList();;
    public boolean isNil = false;
  }

  public boolean isNil = false;
  public List<Var> vars = Lists.newArrayList();
  public List<DataBlockValueOrNIL> list = Lists.newArrayList();
}
