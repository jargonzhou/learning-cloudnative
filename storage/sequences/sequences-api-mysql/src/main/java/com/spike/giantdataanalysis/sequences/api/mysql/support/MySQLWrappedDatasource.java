package com.spike.giantdataanalysis.sequences.api.mysql.support;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;

public final class MySQLWrappedDatasource implements IWrappedMySQLDatasource {

  private DruidDataSource datasource;

  public MySQLWrappedDatasource(DruidDataSourceConfiguration configuration) throws SQLException {
    datasource = new DruidDataSource(configuration.isFairLock());

    datasource.setUrl(configuration.getUrl());
    datasource.setUsername(configuration.getUsername());
    datasource.setPassword(configuration.getPassword());
    datasource.setDriverClassName(configuration.getDriverClassName());
    datasource.setFilters(configuration.getFilters());
    datasource.setMaxActive(configuration.getMaxActive());
    datasource.setInitialSize(configuration.getInitialSize());
    datasource.setMaxWait(configuration.getMaxWait());
    datasource.setMinIdle(configuration.getMinIdle());
    datasource.setTimeBetweenEvictionRunsMillis(configuration.getTimeBetweenEvictionRunsMillis());
    datasource.setMinEvictableIdleTimeMillis(configuration.getMinEvictableIdleTimeMillis());
    datasource.setTestWhileIdle(configuration.isTestWhileIdle());
    datasource.setTestOnBorrow(configuration.isTestOnBorrow());
    datasource.setTestOnReturn(configuration.isTestOnReturn());
    datasource.setPoolPreparedStatements(configuration.isPoolPreparedStatements());
    datasource.setMaxOpenPreparedStatements(configuration.getMaxOpenPreparedStatements());
    datasource.setAsyncInit(configuration.isAsyncInit());

  }

  @Override
  public void init() throws SQLException {
    datasource.init();
  }

  @Override
  public Connection getConnection() throws SQLException {
    return datasource.getConnection();
  }

  @Override
  public void close() throws IOException {
    datasource.close();
  }

}
