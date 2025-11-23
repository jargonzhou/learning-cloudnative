package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

// [36] Move ::= 'MOVE' 'SILENT'? GraphOrDefault 'TO' GraphOrDefault
public class Move {
  public GraphOrDefault graphOrDefaultFirst;
  public boolean isSilent = false;
  public GraphOrDefault graphOrDefaultSecond;
}