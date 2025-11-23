package com.spike.giantdataanalysis.sequences.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class SequenceGroup implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String name;
  private List<Long> values = new ArrayList<>(10);

  public SequenceGroup(String name) {
    this.name = name;
  }

  // ======================================== helper method

  public long min() {
    if (this.size() == 0) {
      return Sequence.INVALID_VALUE;
    }
    return values.get(0);
  }

  public int size() {
    return values.size();
  }

  public long max() {
    if (this.size() == 0) {
      return Sequence.INVALID_VALUE;
    }
    return values.get(this.size() - 1);
  }

  private boolean isValid() {
    if (name == null || "".equals(name.trim())) return false;
    int len = this.size();
    if (len == 0) return false;
    for (int i = 0; i <= len - 2; i++) {
      if (values.get(i) > values.get(i + 1)) {
        return false;
      }
    }

    return true;
  }

  // ======================================== getter/setter
  public String getName() {
    return name;
  }

  public List<Long> getValues() {
    return values;
  }

  public void addValue(long value) {
    if (this.size() != 0 && this.max() > value) {
      throw new RuntimeException("in valid argument: should add in asc order!");
    }

    values.add(value);
  }

  public void setValues(List<Long> values) {
    if (!this.isValid()) throw new RuntimeException("in valid argument: should in asc order!");

    this.values = values;
  }

  @Override
  public String toString() {
    return "SequenceGroup [name=" + name + ", values=" + values + "]";
  }

}
