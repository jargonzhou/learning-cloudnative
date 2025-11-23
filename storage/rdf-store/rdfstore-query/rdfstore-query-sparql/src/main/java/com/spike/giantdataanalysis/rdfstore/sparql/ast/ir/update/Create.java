package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

// [34] Create ::= 'CREATE' 'SILENT'? GraphRef
public class Create {
  public boolean isSilent = false;
  public GraphRef graphRef;
}