package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;

// [31] Load ::= 'LOAD' 'SILENT'? iri ( 'INTO' GraphRef )?
public class Load {
  public boolean isSilent = false;
  public Iri iri;
  public GraphRef graphRef;
}
