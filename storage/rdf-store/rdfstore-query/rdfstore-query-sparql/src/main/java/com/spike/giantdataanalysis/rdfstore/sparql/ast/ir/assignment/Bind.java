package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment;

import java.beans.Expression;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Var;

// [60] Bind ::= 'BIND' '(' Expression 'AS' Var ')'
public class Bind {
  public Expression expression;
  public Var var;
}