package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query;

import java.util.List;

import com.google.common.collect.Lists;

// [4] Prologue ::= ( BaseDecl | PrefixDecl )*
public class Prologue {
  public List<BaseDecl> baseDecls = Lists.newArrayList();
  public List<PrefixDecl> prefixDecls = Lists.newArrayList();
}