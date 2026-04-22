package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad.QuadData;

// [39] DeleteData ::= 'DELETE DATA' QuadData
public class DeleteData {
  public QuadData quadData;
}