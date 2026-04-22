package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrIri;

// [59] ServiceGraphPattern ::= 'SERVICE' 'SILENT'? VarOrIri GroupGraphPattern
public class ServiceGraphPattern {
  public VarOrIri varOrIri;
  public GroupGraphPattern groupGraphPattern;
}