package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment.ValuesClause;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier.SolutionModifier;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.select.SelectClause;

// [8] SubSelect ::= SelectClause WhereClause SolutionModifier ValuesClause
public class SubSelect {
  public SelectClause selectClause;
  public WhereClause whereClause;
  public SolutionModifier solutionModifier;
  public ValuesClause valuesClause;
}