package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

// [124] StrReplaceExpression ::= 'REPLACE' '(' Expression ',' Expression ',' Expression
// ( ',' Expression )? ')'
public class StrReplaceExpression {
  public Expression expression1;
  public Expression expression2;
  public Expression expression3;
  public Expression expression4;
}