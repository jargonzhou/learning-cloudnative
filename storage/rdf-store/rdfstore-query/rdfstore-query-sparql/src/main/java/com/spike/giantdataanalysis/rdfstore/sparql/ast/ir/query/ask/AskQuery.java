package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.ask;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.WhereClause;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier.SolutionModifier;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.select.DatasetClause;

// [12] AskQuery ::= 'ASK' DatasetClause* WhereClause SolutionModifier
public class AskQuery {
  public List<DatasetClause> datasetClauses = Lists.newArrayList();
  public WhereClause whereClause;
  public SolutionModifier solutionModifier;
}