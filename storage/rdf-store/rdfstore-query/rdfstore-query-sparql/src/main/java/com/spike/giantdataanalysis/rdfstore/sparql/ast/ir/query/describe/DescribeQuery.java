package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.describe;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.WhereClause;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier.SolutionModifier;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.select.DatasetClause;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.VarOrIri;

// [11] DescribeQuery ::= 'DESCRIBE' ( VarOrIri+ | '*' ) DatasetClause* WhereClause?
// SolutionModifier
public class DescribeQuery {
  public boolean all = false;
  public List<VarOrIri> varOrIris = Lists.newArrayList();
  public List<DatasetClause> datasetClauses = Lists.newArrayList();
  public WhereClause whereClause;
  public SolutionModifier solutionModifier;
}