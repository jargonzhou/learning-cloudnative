package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.NumericLiteral;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.RDFLiteral;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Var;

// [119] PrimaryExpression ::= BrackettedExpression | BuiltInCall | iriOrFunction | RDFLiteral
// | NumericLiteral | BooleanLiteral | Var
public class PrimaryExpression {
  public enum PrimaryExpressionType {
    BrackettedExpression, BuiltInCall, iriOrFunction, RDFLiteral, NumericLiteral, BooleanLiteral,
    Var
  }

  public PrimaryExpressionType type;
  public BrackettedExpression brackettedExpression;
  public NumericLiteral numericLiteral;
  public Boolean booleanLiteral;
  public RDFLiteral rDFLiteral;
  public BuiltInCall builtInCall;
  public IriOrFunction iriOrFunction;
  public Var var;
}