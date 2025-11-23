package com.spike.giantdataanalysis.sequences.api;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.sequences.api.StaticSequencer.SequenceCategory;
import com.spike.giantdataanalysis.sequences.api.StaticSequencer.SequenceException;

public class TestStaticSequencer {
  private static final Logger LOG = LoggerFactory.getLogger(TestStaticSequencer.class);

  @Test(expected = SequenceException.class)
  public void bordercase_length1() {
    int length = 1;
    String start = "0";
    while (true) {
      LOG.debug(start);
      // start = StaticSequencer.next(SequenceCategory.SPECIAL_CHARACTER, length, start, 3);
      start = StaticSequencer.next(SequenceCategory.NUMBER, length, start, 1);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void bordercase_invalidStartValue() {
    StaticSequencer.next(SequenceCategory.NUMBER, 2, "0W", 1);
  }

  @Test(expected = SequenceException.class)
  public void bordercase_length2() {
    int length = 2;
    String start = "00";// 0W
    while (true) {
      LOG.debug(start);
      // start = StaticSequencer.next(SequenceCategory.SPECIAL_CHARACTER, length, start, 3);
      start = StaticSequencer.next(SequenceCategory.NUMBER, length, start, 1);
    }
  }

  @Test(expected = SequenceException.class)
  public void bordercase_largeStep() {
    int length = 3;
    String start = "000";
    start = "111";
    while (true) {
      LOG.debug(start);
      // start = StaticSequencer.next(SequenceCategory.SPECIAL_CHARACTER, length, start, 100);
      // start = StaticSequencer.next(SequenceCategory.NUMBER, length, start, 100);
      start = StaticSequencer.next(SequenceCategory.SPECIAL_NUMBER, length, start, 89);
    }
  }
}
