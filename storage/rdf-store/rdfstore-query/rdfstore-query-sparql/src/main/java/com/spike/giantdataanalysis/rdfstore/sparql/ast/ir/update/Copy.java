package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

// [37] Copy ::= 'COPY' 'SILENT'? GraphOrDefault 'TO' GraphOrDefault
public class Copy {
  public GraphOrDefault graphOrDefault1;
  public boolean isSilent = false;
  public GraphOrDefault graphOrDefault2;
}