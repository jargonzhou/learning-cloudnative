package com.spike.giantdataanalysis.sequences.faultmodel.store;

// 模拟 修复逻辑存储组中的坏页
class StoreRepairProcess implements Runnable {

  private final Stores stores;

  public StoreRepairProcess(Stores stores) {
    this.stores = stores;
  }

  @Override
  public void run() {
    while (true) {

      int nplex = StoreConfiguration.NPLEX;

      byte[] values = new byte[StoreConfiguration.PAGE_SIZE];

      for (int groupIndex = 0; groupIndex < nplex; groupIndex++) {
        for (int pageIndex = 0; pageIndex < StoreConfiguration.PAGE_NUMBER_IN_STORE; pageIndex++) {
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException e) {
          }

          stores.reliable_store_read(groupIndex, pageIndex, values);
        }
      }
    }
  }

}
