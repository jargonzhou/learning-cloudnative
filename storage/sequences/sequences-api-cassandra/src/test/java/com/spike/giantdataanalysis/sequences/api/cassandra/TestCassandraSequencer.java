package com.spike.giantdataanalysis.sequences.api.cassandra;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.spike.giantdataanalysis.sequences.api.ISequencer;
import com.spike.giantdataanalysis.sequences.api.cassandra.support.CassandraWrappedSession;
import com.spike.giantdataanalysis.sequences.api.cassandra.support.DatastaxCassandraSessionConfiguration;
import com.spike.giantdataanalysis.sequences.api.cassandra.support.IWrappedCassandraSession;
import com.spike.giantdataanalysis.sequences.api.domain.Sequence;
import com.spike.giantdataanalysis.sequences.api.domain.SequenceGroup;

public class TestCassandraSequencer {

  private static IWrappedCassandraSession session;
  private static ISequencer sequencer;

  @BeforeClass
  public static void setupBeforeClass() throws IOException {
    DatastaxCassandraSessionConfiguration configuration =
        new DatastaxCassandraSessionConfiguration();
    session = new CassandraWrappedSession(configuration);
    session.init();
    sequencer = new CassandraSequencer(session);
  }

  @AfterClass
  public static void tearDownAfterClass() throws IOException {
    if (session != null) session.close();
  }

  @Test
  public void test() {
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
