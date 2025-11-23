package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.select;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.WhereClause;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier.SolutionModifier;

// [7] SelectQuery ::= SelectClause DatasetClause* WhereClause SolutionModifier
public class SelectQuery {
  public SelectClause selectClause;
  public List<DatasetClause> datasetClauses = Lists.newArrayList();
  public WhereClause whereClause;
  public SolutionModifier solutionModifier;
}