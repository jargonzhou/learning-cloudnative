package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.query.Prologue;

// [29] Update ::= Prologue ( Update1 ( ';' Update )? )?
public class Update {
  public Prologue prologue;
  public Update1 update1;
  public Update update;
}