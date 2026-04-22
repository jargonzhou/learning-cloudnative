package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

// [69] Constraint ::= BrackettedExpression | BuiltInCall | FunctionCall
public class Constraint {
  public BrackettedExpression brackettedExpression;
  public BuiltInCall builtInCall;
  public FunctionCall functionCall;
}