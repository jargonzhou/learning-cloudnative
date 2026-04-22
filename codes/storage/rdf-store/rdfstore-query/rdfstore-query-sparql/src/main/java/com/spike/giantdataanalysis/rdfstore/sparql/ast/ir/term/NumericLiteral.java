package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term;

import java.math.BigDecimal;
import java.math.BigInteger;

// [130] NumericLiteral ::= NumericLiteralUnsigned | NumericLiteralPositive | NumericLiteralNegative
// [131] NumericLiteralUnsigned ::= INTEGER | DECIMAL | DOUBLE
// [132] NumericLiteralPositive ::= INTEGER_POSITIVE | DECIMAL_POSITIVE | DOUBLE_POSITIVE
// [133] NumericLiteralNegative ::= INTEGER_NEGATIVE | DECIMAL_NEGATIVE | DOUBLE_NEGATIVE
public class NumericLiteral {
  public BigInteger intValue;
  public BigDecimal decimalValue;
}