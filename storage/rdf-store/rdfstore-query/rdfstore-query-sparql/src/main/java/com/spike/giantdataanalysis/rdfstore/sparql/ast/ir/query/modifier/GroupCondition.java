package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.assignment.Bind;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint.BuiltInCall;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint.FunctionCall;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Var;

// [20] GroupCondition ::= BuiltInCall | FunctionCall | '(' Expression ( 'AS' Var )? ')' | Var
public class GroupCondition {
  public BuiltInCall builtInCall;
  public FunctionCall functionCall;
  public Bind varBind;
  public Var var;
}

