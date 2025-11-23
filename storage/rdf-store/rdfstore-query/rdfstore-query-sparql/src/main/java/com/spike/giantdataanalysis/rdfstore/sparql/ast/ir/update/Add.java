package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

// [35] Add ::= 'ADD' 'SILENT'? GraphOrDefault 'TO' GraphOrDefault
public class Add {
  public GraphOrDefault graphOrDefaultFirst;
  public boolean isSilent = false;
  public GraphOrDefault graphOrDefaultSecond;
}