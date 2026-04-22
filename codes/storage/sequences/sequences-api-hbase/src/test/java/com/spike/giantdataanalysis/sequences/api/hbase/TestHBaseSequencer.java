package com.spike.giantdataanalysis.sequences.api.hbase;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.sequences.api.ISequencer;
import com.spike.giantdataanalysis.sequences.api.domain.Sequence;
import com.spike.giantdataanalysis.sequences.api.domain.SequenceGroup;
import com.spike.giantdataanalysis.sequences.api.hbase.support.HBaseWrappedConnection;
import com.spike.giantdataanalysis.sequences.api.hbase.support.IWrappedHBaseConnection;

public class TestHBaseSequencer {
  private static Logger LOG = LoggerFactory.getLogger(TestHBaseSequencer.class);

  private static IWrappedHBaseConnection connection;
  private static ISequencer sequencer;

  @BeforeClass
  public static void setUpBeforeClass() {
    try {
      connection = new HBaseWrappedConnection();
      connection.init();
      sequencer = new HBaseSequencer(connection);
    } catch (IOException e) {
      LOG.error("", e);
    }
  }

  @AfterClass
  public static void tearDownAfterClass() throws IOException {
    if (connection != null) connection.close();
  }

  @Test
  public void test() throws IOException {
    String name = "GLOBAL";
    sequencer.init(name, ISequencer.START_VALUE);

    Sequence current = sequencer.current(name);
    System.out.println(current);

    Sequence next = sequencer.next(name);
    System.out.println(next);
    Assert.assertEquals(1L, next.getValue() - current.getValue());

    SequenceGroup sg = sequencer.next(name, 2);
    System.out.println(sg);
    Assert.assertEquals(2, sg.size());
    Assert.assertEquals(1L, sg.min() - next.getValue());
    Assert.assertEquals(2L, sg.max() - next.getValue());

  }
}
