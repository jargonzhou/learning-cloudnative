package com.spike.giantdataanalysis.sequences.faultmodel.store;

public final class StoreConfiguration {
  // ======================================== constants
  static int STORE_NUMBER = 10000; // 物理存储的数量
  static int PAGE_NUMBER_IN_STORE = 10000; // 一个物理存储中物理页的数量
  static int PAGE_SIZE = 1024 * 8; // 物理页的大小: 8 KB
  static double PWF = 1.0d / 1000000; // 无效写的概率
  static int DUMP_LINE_SIZE = 128; // snapshot查看的一行大小

  static double DECAY_mttvf = 7 * 10000; // 页出错平均时间: 几天
  static double DECAY_mttsf = 100000000; // 存储错误平均时间: 几年

  static int NPLEX = 2; // 逻辑存储组数量

  public void makeStatic() {
    STORE_NUMBER = store_number;
    PAGE_NUMBER_IN_STORE = page_number_in_store;
    PAGE_SIZE = page_size;
    PWF = pwf;
    DUMP_LINE_SIZE = dump_line_size;
    DECAY_mttvf = decay_mttvf;
    DECAY_mttsf = decay_mttsf;
    NPLEX = nplex;
  }

  public static StoreConfiguration _default() {
    return new StoreConfiguration();
  }

  // ======================================== fields

  private int store_number = STORE_NUMBER;
  private int page_number_in_store = PAGE_NUMBER_IN_STORE;
  private int page_size = PAGE_SIZE;
  private double pwf = PWF;
  private int dump_line_size = DUMP_LINE_SIZE;
  private double decay_mttvf = DECAY_mttvf;
  private double decay_mttsf = DECAY_mttsf;
  private int nplex = NPLEX;

  public int getStore_number() {
    return store_number;
  }

  public void setStore_number(int store_number) {
    this.store_number = store_number;
  }

  public int getPage_number_in_store() {
    return page_number_in_store;
  }

  public void setPage_number_in_store(int page_number_in_store) {
    this.page_number_in_store = page_number_in_store;
  }

  public int getPage_size() {
    return page_size;
  }

  public void setPage_size(int page_size) {
    this.page_size = page_size;
  }

  public double getPwf() {
    return pwf;
  }

  public void setPwf(double pwf) {
    this.pwf = pwf;
  }

  public int getDump_line_size() {
    return dump_line_size;
  }

  public void setDump_line_size(int dump_line_size) {
    this.dump_line_size = dump_line_size;
  }

  public double getDecay_mttvf() {
    return decay_mttvf;
  }

  public void setDecay_mttvf(double decay_mttvf) {
    this.decay_mttvf = decay_mttvf;
  }

  public double getDecay_mttsf() {
    return decay_mttsf;
  }

  public void setDecay_mttsf(double decay_mttsf) {
    this.decay_mttsf = decay_mttsf;
  }

  public int getNplex() {
    return nplex;
  }

  public void setNplex(int nplex) {
    this.nplex = nplex;
  }

}