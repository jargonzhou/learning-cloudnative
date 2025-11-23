package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

// [32] Clear ::= 'CLEAR' 'SILENT'? GraphRefAll
public class Clear {
  public boolean isSilent = false;
  public GraphRefAll graphRefAll;
}