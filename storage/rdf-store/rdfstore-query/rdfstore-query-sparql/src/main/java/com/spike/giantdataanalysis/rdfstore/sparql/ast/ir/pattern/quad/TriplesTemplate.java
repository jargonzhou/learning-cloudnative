package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple.TriplesSameSubject;

// [52] TriplesTemplate ::= TriplesSameSubject ( '.' TriplesTemplate? )?
public class TriplesTemplate {
  public TriplesSameSubject triplesSameSubject;
  public TriplesTemplate triplesTemplate;
}