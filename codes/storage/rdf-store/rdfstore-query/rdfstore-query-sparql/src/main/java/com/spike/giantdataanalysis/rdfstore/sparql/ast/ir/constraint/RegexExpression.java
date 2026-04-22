package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

// [122] RegexExpression ::= 'REGEX' '(' Expression ',' Expression ( ',' Expression )? ')'
public class RegexExpression {
  public Expression expression1;
  public Expression expression2;
  public Expression expression3;
}