package com.spike.giantdataanalysis.sequences.faultmodel.message;

public final class MessageConfiguration {

  // ======================================== constants
  static int MESSAGE_DATA_SIZE = 100;
  static long MESSAGE_LISTEN_WAIT_DURATION = 1000L; // 消息监听时无数据等待时间
  static double MESSAGE_pmf = 1d / 100000; // 消息失败(丢失或损坏)的概率
  static double MESSAGE_pmd = 1d / 100000; // 消息重复的概率

  static int PROCESS_NUMBER = 100; // 系统中进程数量
  static long PROCESS_WAIT_ACK_DURATION_MS = 1000L; // 等待ack消息的时间
  static int PROCESS_PROGRAM_SIZE = 10000; // 进程程序区域大小
  static int PROCESS_DATA_SIZE = 10000;// 进程数据区域大小
  static double PROCESS_mttpf = 10000000; // 进程平均失败时间
  static double PROCESS_mttpr = 10000; // 进程修复的平均时间

  static int DUMP_LINE_SIZE = 128; // snapshot查看的一行大小
  // ======================================== fields
}
