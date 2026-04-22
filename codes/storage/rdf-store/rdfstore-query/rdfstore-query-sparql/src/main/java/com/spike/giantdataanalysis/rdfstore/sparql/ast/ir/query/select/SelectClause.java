package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.select;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment.Bind;

// [9] SelectClause ::= 'SELECT' ( 'DISTINCT' | 'REDUCED' )?
// ( ( Var | ( '(' Expression 'AS' Var ')' ) )+ | '*' )
public class SelectClause {
  public boolean distinct = false;
  public boolean reduced = false;
  public List<Bind> vars = Lists.newArrayList();
  public boolean all = false; // *
}