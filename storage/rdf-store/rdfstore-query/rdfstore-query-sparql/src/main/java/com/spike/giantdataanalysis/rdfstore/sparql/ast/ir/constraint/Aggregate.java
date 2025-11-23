package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

// [127] Aggregate ::= 'COUNT' '(' 'DISTINCT'? ( '*' | Expression ) ')'
// | 'SUM' '(' 'DISTINCT'? Expression ')'
// | 'MIN' '(' 'DISTINCT'? Expression ')'
// | 'MAX' '(' 'DISTINCT'? Expression ')'
// | 'AVG' '(' 'DISTINCT'? Expression ')'
// | 'SAMPLE' '(' 'DISTINCT'? Expression ')'
// | 'GROUP_CONCAT' '(' 'DISTINCT'? Expression ( ';' 'SEPARATOR' '=' String )? ')'
public class Aggregate {
  public enum AggregateTypeEnum {
    COUNT, COUNT_DISTINCT, SUM, MIN, MAX, AVG, SAMPLE, GROUP_CONCAT, GROUP_CONCAT_DISTINCT
  }

  public AggregateTypeEnum type;
  public boolean isAll = false;
  public Expression expression;
  public String separator;
}
