package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.NumericLiteral;

// [116] AdditiveExpression ::= MultiplicativeExpression
// (
// '+' MultiplicativeExpression
// | '-' MultiplicativeExpression
// | ( NumericLiteralPositive | NumericLiteralNegative )
// ( ( '*' UnaryExpression ) | ( '/' UnaryExpression ) )*
// )*
public class AdditiveExpression {
  public enum OpEnum {
    ADD, SUBTRACT
  }

  public class AdditiveExpression2 {
    // branch 1
    public OpEnum op; // +,-
    // branch 2
    public MultiplicativeExpression multiplicativeExpression;
    // branch 3
    public NumericLiteral numericLiteral;
    public List<MultiplicativeExpression.UnaryExpressionWithOp> unaryExpressions =
        Lists.newArrayList();
  }

  public MultiplicativeExpression multiplicativeExpression;
  public List<AdditiveExpression2> additiveExpression2s = Lists.newArrayList();
}
