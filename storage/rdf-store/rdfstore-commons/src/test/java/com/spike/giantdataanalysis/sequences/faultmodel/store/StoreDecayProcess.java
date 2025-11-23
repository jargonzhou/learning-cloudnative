package com.spike.giantdataanalysis.sequences.faultmodel.store;

import java.util.Date;
import java.util.Random;

// 模拟 存储和页衰减
class StoreDecayProcess implements Runnable {

  private final Stores stores;

  public StoreDecayProcess(Stores stores) {
    this.stores = stores;
  }

  @Override
  public void run() {

    long now = new Date().getTime();
    long nextStoreFailTime = (long) (now + StoreConfiguration.DECAY_mttsf);
    long nextPageFailTime = (long) (now + StoreConfiguration.DECAY_mttvf);

    while (true) {
      now = new Date().getTime();
      if (now < Math.min(nextPageFailTime, nextStoreFailTime)) {
        try {
          Thread.sleep(100L);
        } catch (InterruptedException e) {
        }
        continue;
      }

      if (now > nextStoreFailTime) {
        int pickedStoreIndex =
            (int) (StoreConfiguration.STORE_NUMBER * new Random(now).nextDouble());
        Store pickedStore = stores.stores[pickedStoreIndex];
        pickedStore.status = false;
        for (Page page : pickedStore.pages) { // 存储下页一并失效
          page.status = false;
        }

        nextStoreFailTime = (long) (now + StoreConfiguration.DECAY_mttsf);
      }

      if (now > nextPageFailTime) {
        int pickedStoreIndex =
            (int) (StoreConfiguration.STORE_NUMBER * new Random(now).nextDouble());
        Store pickedStore = stores.stores[pickedStoreIndex];

        int pickedPageIndex =
            (int) (StoreConfiguration.PAGE_NUMBER_IN_STORE * new Random(now).nextDouble());
        Page pickedPage = pickedStore.pages[pickedPageIndex];
        if (pickedPage != null) {
          pickedPage.status = false;
        }

        nextPageFailTime = (long) (now + StoreConfiguration.DECAY_mttvf);
      }

    }

  }
}