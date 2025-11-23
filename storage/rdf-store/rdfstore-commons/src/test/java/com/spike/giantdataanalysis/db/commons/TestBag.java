package com.spike.giantdataanalysis.db.commons;

import java.util.List;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * Spike unit test of Bag.
 */
public class TestBag {
  static class Relation {
    public String name = "";
    public List<String> fieldNames = Lists.newArrayList();
    private Bag<Tuple> tuples = new HashBag<Tuple>();

    public Relation(String name, String... fieldNames) {
      this.name = name;
      for (String fieldName : fieldNames) {
        this.fieldNames.add(fieldName);
      }
    }

    public void addTuple(int... fields) {
      tuples.add(Tuple.NEW(fields));
    }

    @Override
    public String toString() {
      return name + System.lineSeparator() + Joiner.on("\t").join(fieldNames)
          + System.lineSeparator() + Joiner.on("\n").join(tuples);
    }

  }

  static class Tuple {
    public List<Integer> fields = Lists.newArrayList();

    public Tuple(List<Integer> fields) {
      this.fields = fields;
    }

    public Tuple(int... fields) {
      for (int field : fields) {
        this.fields.add(field);
      }
    }

    public static Tuple NEW(int... fields) {
      return new Tuple(fields);
    }

    @Override
    public String toString() {
      return Joiner.on("\t").join(fields);
    }
  }

  static class Field {
    public int value;

    public Field(int value) {
      this.value = value;
    }
  }

  @Test
  public void test() {

    Relation R_5_1 = new Relation("R_5_1", "A", "B");
    R_5_1.addTuple(1, 2);
    R_5_1.addTuple(3, 4);
    R_5_1.addTuple(1, 2);
    R_5_1.addTuple(1, 2);
    System.out.println(R_5_1);

    Relation R_5_2 = new Relation("R_5_2", "A", "B", "C");
    R_5_2.addTuple(1, 2, 5);
    R_5_2.addTuple(3, 4, 6);
    R_5_2.addTuple(1, 2, 7);
    R_5_2.addTuple(1, 2, 8);
    System.out.println(R_5_2);

    // example 5.4
    Relation S = new Relation("S", "A", "B");
    S.addTuple(1, 2);
    S.addTuple(3, 4);
    S.addTuple(3, 4);
    S.addTuple(5, 6);
  }
}
