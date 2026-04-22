package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Iri;

// [45] GraphOrDefault ::= 'DEFAULT' | 'GRAPH'? iri
public class GraphOrDefault {
  public boolean isDefault = false;
  public Iri iri;
}