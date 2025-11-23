package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import java.util.List;

import com.google.common.collect.Lists;

// [117] MultiplicativeExpression ::= UnaryExpression ( '*' UnaryExpression | '/' UnaryExpression )*
public class MultiplicativeExpression {
  public enum OpEnum {
    MULTIPLY, DIVIDE
  }

  public class UnaryExpressionWithOp {
    public OpEnum op; // *, /
    public UnaryExpression unaryExpression;
  }

  public UnaryExpression unaryExpression1;
  public List<UnaryExpressionWithOp> unaryExpressions = Lists.newArrayList();
}
