package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [77] PropertyListNotEmpty ::= Verb ObjectList ( ';' ( Verb ObjectList )? )*
public class PropertyListNotEmpty {
  public class VerbObjectList {
    public Verb verb;
    public ObjectList objectList;
  }

  public List<VerbObjectList> verbObjectLists = Lists.newArrayList();
}
