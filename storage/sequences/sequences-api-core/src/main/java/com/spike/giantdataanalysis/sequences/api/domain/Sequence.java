package com.spike.giantdataanalysis.sequences.api.domain;

import java.io.Serializable;

public final class Sequence implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final long INVALID_VALUE = -1L;

  private final String name;
  private final long value;

  public Sequence(String name, long value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public long getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "Sequence [name=" + name + ", value=" + value + "]";
  }

}
