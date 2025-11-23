package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

// [118] UnaryExpression ::= '!' PrimaryExpression
// | '+' PrimaryExpression
// | '-' PrimaryExpression
// | PrimaryExpression
public class UnaryExpression {
  enum PrefixEnum {
    COMPLEMENT, POSITIVE, NEGATIVE, NONE
  }

  public PrefixEnum prefixEnum;
  public PrimaryExpression primaryExpression;
}
