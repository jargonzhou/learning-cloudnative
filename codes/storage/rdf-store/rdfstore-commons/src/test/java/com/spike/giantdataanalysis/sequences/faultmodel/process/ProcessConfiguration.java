package com.spike.giantdataanalysis.sequences.faultmodel.process;

public final class ProcessConfiguration {

  // ======================================== constants
  static int MESSAGE_DATA_SIZE = 100;
  static double MESSAGE_pmf = 1d / 100000; // 消息失败(丢失或损坏)的概率
  static double MESSAGE_pmd = 1d / 100000; // 消息重复的概率

  static int PROCESS_NUMBER = 100; // 系统中进程数量
  static int PROCESS_PROGRAM_SIZE = 10000; // 进程程序区域大小
  static int PROCESS_DATA_SIZE = 10000;// 进程数据区域大小
  // static int PROCESS_MESSAGE_SIZE = 100; // 进程中输入消息队列的大小
  static double PROCESS_mttpf = 10000000; // 进程平均失败时间
  static double PROCESS_mttpr = 10000; // 进程修复的平均时间

  static int DUMP_LINE_SIZE = 128; // snapshot查看的一行大小

  // static int PAGE_SIZE = 16; // 物理页的大小

  public static ProcessConfiguration _default() {
    return new ProcessConfiguration();
  }

  public void makeStatic() {
    MESSAGE_DATA_SIZE = message_data_size;
    MESSAGE_pmf = message_pmf;
    MESSAGE_pmd = message_pmd;
    PROCESS_NUMBER = process_number;
    PROCESS_PROGRAM_SIZE = process_program_size;
    PROCESS_DATA_SIZE = process_data_size;
    // PROCESS_MESSAGE_SIZE = process_message_size;
    PROCESS_mttpf = process_mttpf;
    PROCESS_mttpr = process_mttpr;
    DUMP_LINE_SIZE = dump_line_size;
  }

  // ======================================== fields

  private int message_data_size = MESSAGE_DATA_SIZE;
  private double message_pmf = MESSAGE_pmf;
  private double message_pmd = MESSAGE_pmd;
  private int process_number = PROCESS_NUMBER;
  private int process_program_size = PROCESS_PROGRAM_SIZE;
  private int process_data_size = PROCESS_DATA_SIZE;
  // private int process_message_size = PROCESS_MESSAGE_SIZE;
  private double process_mttpf = PROCESS_mttpf;
  private double process_mttpr = PROCESS_mttpr;
  private int dump_line_size = DUMP_LINE_SIZE;

  public int getMessage_data_size() {
    return message_data_size;
  }

  public void setMessage_data_size(int message_data_size) {
    this.message_data_size = message_data_size;
  }

  public double getMessage_pmf() {
    return message_pmf;
  }

  public void setMessage_pmf(double message_pmf) {
    this.message_pmf = message_pmf;
  }

  public double getMessage_pmd() {
    return message_pmd;
  }

  public void setMessage_pmd(double message_pmd) {
    this.message_pmd = message_pmd;
  }

  public int getProcess_number() {
    return process_number;
  }

  public void setProcess_number(int process_number) {
    this.process_number = process_number;
  }

  public int getProcess_program_size() {
    return process_program_size;
  }

  public void setProcess_program_size(int process_program_size) {
    this.process_program_size = process_program_size;
  }

  public int getProcess_data_size() {
    return process_data_size;
  }

  public void setProcess_data_size(int process_data_size) {
    this.process_data_size = process_data_size;
  }

  public double getProcess_mttpf() {
    return process_mttpf;
  }

  public void setProcess_mttpf(double process_mttpf) {
    this.process_mttpf = process_mttpf;
  }

  public double getProcess_mttpr() {
    return process_mttpr;
  }

  public void setProcess_mttpr(double process_mttpr) {
    this.process_mttpr = process_mttpr;
  }

  public int getDump_line_size() {
    return dump_line_size;
  }

  public void setDump_line_size(int dump_line_size) {
    this.dump_line_size = dump_line_size;
  }
}
