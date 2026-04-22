package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple.TriplesSameSubjectPath;

// [55] TriplesBlock ::= TriplesSameSubjectPath ( '.' TriplesBlock? )?
public class TriplesBlock {
  public TriplesSameSubjectPath triplesSameSubjectPath;
  public TriplesBlock triplesBlock;
}