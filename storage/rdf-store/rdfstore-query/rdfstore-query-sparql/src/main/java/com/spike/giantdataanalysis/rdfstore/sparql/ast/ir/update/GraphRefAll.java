package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

// [47] GraphRefAll ::= GraphRef | 'DEFAULT' | 'NAMED' | 'ALL'
public class GraphRefAll {
  public GraphRef graphRef;
  public boolean isDefault = false;
  public boolean isNamed = false;
  public boolean isAll = false;
}