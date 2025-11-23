package com.spike.giantdataanalysis.sequences.faultmodel.store;

import java.util.Date;

class Page {
  boolean status = false; // 可以使用校验和
  byte[] values = new byte[StoreConfiguration.PAGE_SIZE];
  long version = new Date().getTime(); // 可靠性版本字段: 处理歧义读问题

  void write(byte[] values) {
    System.arraycopy(values, 0, this.values, 0, StoreConfiguration.PAGE_SIZE);
    version = new Date().getTime(); // 更新版本字段
  }

  void read(byte[] values) {
    System.arraycopy(this.values, 0, values, 0, StoreConfiguration.PAGE_SIZE);
  }

  void updateVersion() {
    this.version = new Date().getTime();
  }

}