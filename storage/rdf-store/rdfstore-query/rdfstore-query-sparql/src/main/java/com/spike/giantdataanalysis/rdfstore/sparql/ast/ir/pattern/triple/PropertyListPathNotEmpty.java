package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.pattern.triple;

import java.util.List;

import com.google.common.collect.Lists;

// [83] PropertyListPathNotEmpty ::= ( VerbPath | VerbSimple ) ObjectListPath
// ( ';' ( ( VerbPath | VerbSimple ) ObjectList )? )*
public class PropertyListPathNotEmpty {
  public class VerbObjectList {
    public VerbPath verbPath;
    public VerbSimple verbSimple;
    public ObjectList objectList;
  }

  public VerbPath verbPath;
  public VerbSimple verbSimple;
  public ObjectListPath objectListPath;
  public List<VerbObjectList> list = Lists.newArrayList();
}