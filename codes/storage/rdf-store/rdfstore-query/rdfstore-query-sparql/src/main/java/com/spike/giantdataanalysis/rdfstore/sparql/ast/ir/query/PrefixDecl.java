package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query;

// [6] PrefixDecl ::= 'PREFIX' PNAME_NS IRIREF
public class PrefixDecl {
  public String pnameNS;
  public String iriRef;
}