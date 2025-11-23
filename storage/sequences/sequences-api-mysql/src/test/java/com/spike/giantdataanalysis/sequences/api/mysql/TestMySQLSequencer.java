package com.spike.giantdataanalysis.sequences.api.mysql;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.sequences.api.ISequencer;
import com.spike.giantdataanalysis.sequences.api.domain.Sequence;
import com.spike.giantdataanalysis.sequences.api.domain.SequenceGroup;
import com.spike.giantdataanalysis.sequences.api.mysql.support.DruidDataSourceConfiguration;
import com.spike.giantdataanalysis.sequences.api.mysql.support.IWrappedMySQLDatasource;
import com.spike.giantdataanalysis.sequences.api.mysql.support.MySQLWrappedDatasource;

public class TestMySQLSequencer {
  private static Logger LOG = LoggerFactory.getLogger(TestMySQLSequencer.class);

  private static IWrappedMySQLDatasource datasource;
  private static ISequencer sequencer;

  @BeforeClass
  public static void setUpBeforeClass() {
    try {
      DruidDataSourceConfiguration configuration = new DruidDataSourceConfiguration();
      configuration.setUrl("jdbc:mysql://127.0.0.1:3306/playground");
      configuration.setUsername("root");
      configuration.setPassword("admin");
      datasource = new MySQLWrappedDatasource(configuration);
      datasource.init();
      sequencer = new MySQLSequencer(datasource);
    } catch (SQLException e) {
      LOG.error("", e);
    }
  }

  @AfterClass
  public static void tearDownAfterClass() throws IOException {
    if (datasource != null) datasource.close();
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
