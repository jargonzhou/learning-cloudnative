package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.select;

// [13] DatasetClause ::= 'FROM' ( DefaultGraphClause | NamedGraphClause )
public class DatasetClause {
  public DefaultGraphClause defaultGraphClause;
  public NamedGraphClause namedGraphClause;
}