package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment.ValuesClause;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.ask.AskQuery;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.construct.ConstructQuery;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.describe.DescribeQuery;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.select.SelectQuery;

// [2] Query ::= Prologue
// ( SelectQuery | ConstructQuery | DescribeQuery | AskQuery )
// ValuesClause
public class Query {
  public enum QueryTypeEnum {
    SelectQuery, ConstructQuery, DescribeQuery, AskQuery
  }

  public Prologue prologue;

  public QueryTypeEnum type;
  public SelectQuery selectQuery;
  public ConstructQuery constructQuery;
  public DescribeQuery describeQuery;
  public AskQuery askQuery;

  public ValuesClause valuesClause;
}