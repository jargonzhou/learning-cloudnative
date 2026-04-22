package com.spike.giantdataanalysis.sequences.api.mysql.support;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface IWrappedMySQLDatasource {
  void init() throws SQLException;

  Connection getConnection() throws SQLException;

  void close() throws IOException;
}
