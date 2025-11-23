package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.construct;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.quad.TriplesTemplate;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.WhereClause;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier.SolutionModifier;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.select.DatasetClause;

// [10] ConstructQuery ::= 'CONSTRUCT' ( ConstructTemplate DatasetClause* WhereClause
// SolutionModifier | DatasetClause* 'WHERE' '{' TriplesTemplate? '}' SolutionModifier )
public class ConstructQuery {
  public boolean useConstructTemplate = false;
  public ConstructTemplate constructTemplate;
  public List<DatasetClause> datasetClauses = Lists.newArrayList();
  public WhereClause whereClause;
  public SolutionModifier solutionModifier;

  public TriplesTemplate triplesTemplate;
}