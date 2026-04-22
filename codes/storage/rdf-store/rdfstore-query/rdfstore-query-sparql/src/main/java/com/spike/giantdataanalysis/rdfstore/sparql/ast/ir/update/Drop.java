package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

// [33] Drop ::= 'DROP' 'SILENT'? GraphRefAll
public class Drop {
  public boolean isSilent = false;
  public GraphRefAll graphRefAll;
}