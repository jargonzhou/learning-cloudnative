package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

// [93] PathMod ::= '?' | '*' | '+'
public enum PathMode {
  /** ? */
  ZERO_OR_ONE,
  /** * */
  ZERO_OR_MORE,
  /** + */
  ONE_OR_MORE
}