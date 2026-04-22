package com.spike.giantdataanalysis.rdfstore.sparql.core;

import java.util.Date;

public enum XSDDataTypeEnum {
  DECIMAL(Double.class), //
  INTEGER(Integer.class), //
  DATETIME(Date.class), //
  DATE(Date.class), //
  BOOLEAN(Boolean.class);

  private Class<?> javaType;

  XSDDataTypeEnum(Class<?> javaType) {
    this.javaType = javaType;
  }

  public Class<?> javaType() {
    return javaType;
  }
}
