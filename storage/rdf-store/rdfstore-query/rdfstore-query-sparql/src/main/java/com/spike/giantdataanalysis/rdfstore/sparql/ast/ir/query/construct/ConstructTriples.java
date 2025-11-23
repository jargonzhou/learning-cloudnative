package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.construct;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple.TriplesSameSubject;

// [74] ConstructTriples ::= TriplesSameSubject ( '.' ConstructTriples? )?
public class ConstructTriples {
  public TriplesSameSubject triplesSameSubject;
  public ConstructTriples constructTriples;
}