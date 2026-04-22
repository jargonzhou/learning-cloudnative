package com.spike.giantdataanalysis.sequences.api.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.spike.giantdataanalysis.sequences.api.ISequencer;
import com.spike.giantdataanalysis.sequences.api.cassandra.support.IWrappedCassandraSession;
import com.spike.giantdataanalysis.sequences.api.domain.Sequence;
import com.spike.giantdataanalysis.sequences.api.domain.SequenceGroup;

public class CassandraSequencer implements ISequencer {
  private static final long serialVersionUID = 1L;

  // private static final Logger LOG = LoggerFactory.getLogger(CassandraSequencer.class);

  private final IWrappedCassandraSession session;

  public CassandraSequencer(IWrappedCassandraSession session) {
    this.session = session;
  }

  @Override
  public void init(String name, long value) {
    Sequence current = this.current(name);
    if (current != null) {
      StringBuilder sb = new StringBuilder();
      sb.append("UPDATE sequence ");
      sb.append("SET current_value = current_value - " + current.getValue() + " ");
      sb.append("WHERE name = '" + name + "' ");
      session.execute(sb.toString());
    }

    StringBuilder sb = new StringBuilder();
    sb.append("UPDATE sequence ");
    sb.append("SET current_value = current_value + " + value + " ");
    sb.append("WHERE name = '" + name + "' ");
    session.execute(sb.toString());
  }

  @Override
  public Sequence current(String name) {

    StringBuilder sb = new StringBuilder();
    sb.append("SELECT * ");
    sb.append("FROM sequence ");
    sb.append("WHERE name = '" + name + "' ");
    ResultSet rs = session.execute(sb.toString());

    Row row = rs.one();
    if (row != null) {
      long currentvalue = row.getLong("current_value");
      return new Sequence(name, currentvalue);
    }

    return null;
  }

  @Override
  public synchronized Sequence next(String name) {
    Sequence current = this.current(name);

    StringBuilder sb = new StringBuilder();
    sb.append("UPDATE sequence ");
    sb.append("SET current_value = current_value + 1 ");
    sb.append("WHERE name = '" + name + "' ");
    // sb.append("AND current_value = " + current.getValue()); // try with lock
    session.execute(sb.toString());

    return new Sequence(name, current.getValue() + 1L);
  }

  @Override
  public synchronized SequenceGroup next(String name, int n) {
    Sequence current = this.current(name);

    StringBuilder sb = new StringBuilder();
    sb.append("UPDATE sequence ");
    sb.append("SET current_value = current_value + " + n + " ");
    sb.append("WHERE name = '" + name + "' ");
    // sb.append("AND current_value = " + current.getValue()); // try with lock
    session.execute(sb.toString());

    SequenceGroup sg = new SequenceGroup(name);
    for (int i = 1; i <= n; i++) {
      sg.addValue(current.getValue() + i);
    }

    return sg;
  }

}
