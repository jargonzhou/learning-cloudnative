package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad;

import java.util.List;

import com.google.common.collect.Lists;

// [50] Quads ::= TriplesTemplate? ( QuadsNotTriples '.'? TriplesTemplate? )*
public class Quads {
  public class QuadsNotTriplesAndTriplesTemplate {
    public QuadsNotTriples quadsNotTriples;
    public TriplesTemplate triplesTemplate;
  }

  public TriplesTemplate triplesTemplate;
  public List<QuadsNotTriplesAndTriplesTemplate> list = Lists.newArrayList();
}