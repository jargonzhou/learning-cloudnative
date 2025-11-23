package com.spike.giantdataanalysis.sequences.api.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.sequences.api.ISequencer;
import com.spike.giantdataanalysis.sequences.api.domain.Sequence;
import com.spike.giantdataanalysis.sequences.api.domain.SequenceGroup;
import com.spike.giantdataanalysis.sequences.api.hbase.support.IWrappedHBaseConnection;

public class HBaseSequencer implements ISequencer {
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(HBaseSequencer.class);

  public static String tableName = "playground:sequence";
  public static String family = "cf";
  public static String qualifier = "c";

  private Table table;

  public HBaseSequencer(IWrappedHBaseConnection connection) throws IOException {
    this.table = connection.getTable(tableName);
  }

  @Override
  public void init(String name, long value) {
    try {
      Sequence current = this.current(name);
      if (current != null) {
        table.incrementColumnValue(Bytes.toBytes(name), Bytes.toBytes(family),
          Bytes.toBytes(qualifier), -1 * current.getValue());
      }

      table.incrementColumnValue(Bytes.toBytes(name), Bytes.toBytes(family),
        Bytes.toBytes(qualifier), value);
    } catch (IOException e) {
      LOG.error("", e);
    }
  }

  @Override
  public Sequence current(String name) {

    try {
      Get get = new Get(Bytes.toBytes(name));
      get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
      get.setMaxVersions(1);

      Result result = table.get(get);
      if (result.isEmpty()) {
        return this.next(name);
      }

      return new Sequence(name,
          Bytes.toLong(result.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifier))));
    } catch (IOException e) {
      LOG.error("", e);
    }

    return null;
  }

  @Override
  public Sequence next(String name) {
    try {
      long nextValue = table.incrementColumnValue(Bytes.toBytes(name), Bytes.toBytes(family),
        Bytes.toBytes(qualifier), 1);
      return new Sequence(name, nextValue);
    } catch (IOException e) {
      LOG.error("", e);
    }
    return null;
  }

  @Override
  public SequenceGroup next(String name, int n) {
    try {
      long nextValueEnd = table.incrementColumnValue(Bytes.toBytes(name), Bytes.toBytes(family),
        Bytes.toBytes(qualifier), n);
      SequenceGroup sg = new SequenceGroup(name);
      for (int i = n - 1; i >= 0; i--) {
        sg.addValue(nextValueEnd - i);
      }
      return sg;
    } catch (IOException e) {
      LOG.error("", e);
    }
    return null;
  }

}
