package com.spike.giantdataanalysis.sequences.api.mysql.support;

// settings REF: https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE
public class DruidDataSourceConfiguration {

  private boolean fairLock = true;

  private String url;
  private String username;
  private String password;
  private String driverClassName = "com.mysql.jdbc.Driver";

  private int maxActive = 20;

  private String filters = "stat";
  private int initialSize = 1;
  private long maxWait = 60000;
  private int minIdle = 1;

  private long timeBetweenEvictionRunsMillis = 60000;
  private long minEvictableIdleTimeMillis = 300000;
  private boolean testWhileIdle = true;
  private boolean testOnBorrow = false;
  private boolean testOnReturn = false;

  private boolean poolPreparedStatements = true;
  private int maxOpenPreparedStatements = 20;
  private boolean asyncInit = true;

  /// many others

  public String getUsername() {
    return username;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean isFairLock() {
    return fairLock;
  }

  public void setFairLock(boolean fairLock) {
    this.fairLock = fairLock;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getMaxActive() {
    return maxActive;
  }

  public void setMaxActive(int maxActive) {
    this.maxActive = maxActive;
  }

  public String getFilters() {
    return filters;
  }

  public void setFilters(String filters) {
    this.filters = filters;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  public int getInitialSize() {
    return initialSize;
  }

  public void setInitialSize(int initialSize) {
    this.initialSize = initialSize;
  }

  public long getMaxWait() {
    return maxWait;
  }

  public void setMaxWait(long maxWait) {
    this.maxWait = maxWait;
  }

  public int getMinIdle() {
    return minIdle;
  }

  public void setMinIdle(int minIdle) {
    this.minIdle = minIdle;
  }

  public long getTimeBetweenEvictionRunsMillis() {
    return timeBetweenEvictionRunsMillis;
  }

  public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
  }

  public long getMinEvictableIdleTimeMillis() {
    return minEvictableIdleTimeMillis;
  }

  public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
    this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
  }

  public boolean isTestWhileIdle() {
    return testWhileIdle;
  }

  public void setTestWhileIdle(boolean testWhileIdle) {
    this.testWhileIdle = testWhileIdle;
  }

  public boolean isTestOnBorrow() {
    return testOnBorrow;
  }

  public void setTestOnBorrow(boolean testOnBorrow) {
    this.testOnBorrow = testOnBorrow;
  }

  public boolean isTestOnReturn() {
    return testOnReturn;
  }

  public void setTestOnReturn(boolean testOnReturn) {
    this.testOnReturn = testOnReturn;
  }

  public boolean isPoolPreparedStatements() {
    return poolPreparedStatements;
  }

  public void setPoolPreparedStatements(boolean poolPreparedStatements) {
    this.poolPreparedStatements = poolPreparedStatements;
  }

  public int getMaxOpenPreparedStatements() {
    return maxOpenPreparedStatements;
  }

  public void setMaxOpenPreparedStatements(int maxOpenPreparedStatements) {
    this.maxOpenPreparedStatements = maxOpenPreparedStatements;
  }

  public boolean isAsyncInit() {
    return asyncInit;
  }

  public void setAsyncInit(boolean asyncInit) {
    this.asyncInit = asyncInit;
  }

}
