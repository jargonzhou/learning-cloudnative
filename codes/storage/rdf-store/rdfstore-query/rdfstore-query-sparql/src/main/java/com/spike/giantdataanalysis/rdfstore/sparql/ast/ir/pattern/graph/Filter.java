package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.graph;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint.Constraint;

// [68] Filter ::= 'FILTER' Constraint
public class Filter {
  public Constraint constraint;
}