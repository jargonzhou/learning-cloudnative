package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

// [114] RelationalExpression ::= NumericExpression (
// '=' NumericExpression
// | '!=' NumericExpression
// | '<' NumericExpression
// | '>' NumericExpression
// | '<=' NumericExpression
// | '>=' NumericExpression
// | 'IN' ExpressionList
// | 'NOT' 'IN' ExpressionList
// )?
public class RelationalExpression {
  public enum RelationalExpressionTypeEnum {
    EQ, NEQ, LT, GT, LTE, GTE, IN, NOT_IN
  }

  public NumericExpression numericExpression1;
  public RelationalExpressionTypeEnum type;
  public NumericExpression numericExpression2;
  public ExpressionList expressionList;
}