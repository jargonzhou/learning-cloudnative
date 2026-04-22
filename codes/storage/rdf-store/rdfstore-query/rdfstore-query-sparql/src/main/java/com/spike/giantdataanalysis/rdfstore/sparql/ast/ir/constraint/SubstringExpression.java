package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

// [123] SubstringExpression ::= 'SUBSTR' '(' Expression ',' Expression ( ',' Expression )? ')'
public class SubstringExpression {
  public Expression expression1;
  public Expression expression2;
  public Expression expression3;
}