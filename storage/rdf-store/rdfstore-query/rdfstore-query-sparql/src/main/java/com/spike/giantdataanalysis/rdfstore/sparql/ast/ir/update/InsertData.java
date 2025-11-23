package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad.QuadData;

// [38] InsertData ::= 'INSERT DATA' QuadData
public class InsertData {
  public QuadData quadData;
}