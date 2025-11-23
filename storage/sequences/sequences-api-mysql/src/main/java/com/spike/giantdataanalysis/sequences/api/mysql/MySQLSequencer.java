package com.spike.giantdataanalysis.sequences.api.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.spike.giantdataanalysis.sequences.api.ISequencer;
import com.spike.giantdataanalysis.sequences.api.domain.Sequence;
import com.spike.giantdataanalysis.sequences.api.domain.SequenceGroup;
import com.spike.giantdataanalysis.sequences.api.mysql.support.IWrappedMySQLDatasource;

public final class MySQLSequencer implements ISequencer {
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(MySQLSequencer.class);

  private final IWrappedMySQLDatasource datasource;

  public MySQLSequencer(IWrappedMySQLDatasource datasource) {
    this.datasource = datasource;
  }

  @Override
  public void init(String name, long value) {
    this.query("SELECT seq_setval('" + name + "', " + value + ")");
  }

  @Override
  public Sequence current(String name) {
    String currentValue = this.query("SELECT seq_currval('" + name + "')");
    return new Sequence(name, Integer.valueOf(Splitter.on(",").splitToList(currentValue).get(0)));
  }

  @Override
  public Sequence next(String name) {
    String nextValue = this.query("SELECT seq_nextvaln('" + name + "', 1)");
    int nextValueEnd = Integer.valueOf(Splitter.on(",").splitToList(nextValue).get(0));
    return new Sequence(name, nextValueEnd);
  }

  @Override
  public SequenceGroup next(String name, int n) {
    String nextValue = this.query("SELECT seq_nextvaln('" + name + "', " + n + ")");
    int nextValueEnd = Integer.valueOf(Splitter.on(",").splitToList(nextValue).get(0));
    SequenceGroup sg = new SequenceGroup(name);
    for (int i = n - 1; i >= 0; i--) {
      sg.addValue(nextValueEnd - i);
    }
    return sg;
  }

  private String query(String sql) {
    try {
      Connection conn = datasource.getConnection();
      ResultSet rs = conn.prepareStatement(sql).executeQuery();
      if (rs.next()) {
        return rs.getString(1);
      }
    } catch (SQLException e) {
      LOG.error("execute " + sql + " failed", e);
    }

    return null;
  }

}
