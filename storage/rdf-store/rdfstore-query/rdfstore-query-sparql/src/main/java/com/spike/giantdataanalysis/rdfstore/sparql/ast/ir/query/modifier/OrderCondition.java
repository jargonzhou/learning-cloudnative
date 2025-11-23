package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.modifier;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint.BrackettedExpression;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint.Constraint;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Var;

// [24] OrderCondition ::= ( ( 'ASC' | 'DESC' ) BrackettedExpression )
// | ( Constraint | Var )
public class OrderCondition {
  public boolean isAsc;
  public BrackettedExpression brackettedExpression;
  public Constraint constraint;
  public Var var;
}