package com.spike.giantdataanalysis.sequences.faultmodel.store;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import com.google.common.base.Splitter;
import com.spike.giantdataanalysis.rdfstore.commons.lang.MoreBytes;

/// NEED TO CHECK: when to initialize the Store and Page

/// ASSUMPTIONS:
/// read, write operations are atomic
public final class Stores {

  private volatile boolean initialized = false;
  private final Random random = new Random(new Date().getTime());
  private final StoreConfiguration configuration;
  final Store[] stores = new Store[StoreConfiguration.STORE_NUMBER];

  public Stores(StoreConfiguration configuration) {
    this.configuration = configuration;
    this.configuration.makeStatic();

    this.initialize();
  }

  /**
   * optimistic read from logic store
   * @param groupIndex
   * @param pageIndex
   * @param values OUT
   * @return
   */
  public boolean optimistic_store_read(int groupIndex, int pageIndex, byte[] values) {
    int nplex = StoreConfiguration.NPLEX;
    if (groupIndex >= StoreConfiguration.STORE_NUMBER / StoreConfiguration.NPLEX) return false;
    if (pageIndex >= StoreConfiguration.PAGE_NUMBER_IN_STORE) return false;

    int storeIndex = groupIndex * nplex;
    if (store_read(storeIndex, pageIndex, values)) {
      return true;
    } else {
      // change to reliable read
      return reliable_store_read(groupIndex, pageIndex, values);
    }

  }

  /**
   * reliable write to logic store
   * @param groupIndex
   * @param pageIndex
   * @param values
   * @return
   */
  public boolean reliable_store_write(int groupIndex, int pageIndex, byte[] values) {
    int nplex = StoreConfiguration.NPLEX;
    if (groupIndex >= StoreConfiguration.STORE_NUMBER / StoreConfiguration.NPLEX) return false;
    if (pageIndex >= StoreConfiguration.PAGE_NUMBER_IN_STORE) return false;

    boolean status = false;

    // 写逻辑存储组中的每个存储
    for (int i = 0; i < nplex; i++) {
      int storeIndex = groupIndex * nplex + i;
      status = status || store_write(storeIndex, pageIndex, values);
    }

    return status;
  }

  /**
   * reliable read from logic store
   * <p>
   * <b>Note</b>: read repair
   * @param groupIndex
   * @param pageIndex
   * @param values OUT
   * @return
   */
  public boolean reliable_store_read(int groupIndex, int pageIndex, byte[] values) {
    int nplex = StoreConfiguration.NPLEX;
    if (groupIndex >= StoreConfiguration.STORE_NUMBER / nplex) return false;
    if (pageIndex >= StoreConfiguration.PAGE_NUMBER_IN_STORE) return false;

    boolean gotOne = false; // 是否找到页
    boolean bad = false; // 是否存在坏页
    byte[] next = new byte[StoreConfiguration.PAGE_SIZE];
    long maxVersionSofar = -1L; // 当前遇到的最大的版本

    for (int i = 0; i < nplex; i++) {
      int storeIndex = groupIndex * nplex + i;
      Store store = stores[storeIndex];
      Page page = store.pages[pageIndex];

      boolean status = store_read(storeIndex, pageIndex, next); // 读入next
      long nextVersion = page.version; // next的版本

      if (!status) {
        bad = true;
      } else {
        if (!gotOne) { // 首次读
          System.arraycopy(next, 0, values, 0, StoreConfiguration.PAGE_SIZE);
          gotOne = true;
          maxVersionSofar = nextVersion;
        } else if (maxVersionSofar != nextVersion) { // 非首次读
          bad = true;
          if (nextVersion > maxVersionSofar) {
            System.arraycopy(next, 0, values, 0, StoreConfiguration.PAGE_SIZE);
            maxVersionSofar = nextVersion;
          }
        }
      }
    }

    if (!gotOne) return false;
    if (bad) reliable_store_write(groupIndex, pageIndex, values);

    return true;
  }

  /**
   * write to store
   * @param storeIndex
   * @param pageIndex
   * @param values
   * @return
   */
  public boolean store_write(int storeIndex, int pageIndex, byte[] values) {
    if (storeIndex >= StoreConfiguration.STORE_NUMBER) return false;
    if (pageIndex >= StoreConfiguration.PAGE_NUMBER_IN_STORE) return false;

    Store store = stores[storeIndex];
    if (!store.status) return false;

    if (StoreConfiguration.PWF > random.nextDouble()) return true;

    Page page = store.pages[pageIndex];
    page.status = true;

    // System.arraycopy(values, 0, page.values, 0, Configuration.PAGE_SIZE);
    page.read(values);

    return true;
  }

  /**
   * read from store
   * @param storeIndex
   * @param pageIndex
   * @param values OUT
   * @return
   */
  public boolean store_read(int storeIndex, int pageIndex, byte[] values) {
    if (storeIndex >= StoreConfiguration.STORE_NUMBER) return false;
    if (pageIndex >= StoreConfiguration.PAGE_NUMBER_IN_STORE) return false;

    Store store = stores[storeIndex];
    if (!store.status) return false;
    Page page = store.pages[pageIndex];
    if (!page.status) return false;

    // System.arraycopy(page.values, 0, values, 0, Configuration.PAGE_SIZE);
    page.write(values);

    return true;
  }

  private synchronized void initialize() {
    if (!initialized) {
      Store store = null;
      Page page = null;

      for (int i = 0; i < StoreConfiguration.STORE_NUMBER; i++) {
        store = new Store();
        store.status = true;

        for (int j = 0; j < StoreConfiguration.PAGE_NUMBER_IN_STORE; j++) {
          page = new Page();
          page.status = true;
          page.updateVersion();
          store.pages[j] = page;
        }

        stores[i] = store;
      }
    }
  }

  // ======================================== helper method

  // private Store get(int storeIndex) {
  // if (storeIndex < 0 || storeIndex > Configuration.STORE_NUMBER) return null;
  //
  // return stores[storeIndex];
  // }
  //
  // private Store getCreate(int storeIndex) {
  // if (storeIndex < 0 || storeIndex > Configuration.STORE_NUMBER) return null;
  //
  // Store result = stores[storeIndex];
  // if (result == null) {
  // result = new Store();
  // result.status = true;
  // stores[storeIndex] = result;
  // }
  // return result;
  // }

  /**
   * snapshot
   * @param storeIndex
   * @param pageIndex
   * @return
   */
  public String dump(int storeIndex, int pageIndex) {
    StringBuilder sb = new StringBuilder();
    Store store = stores[storeIndex];
    if (store == null) {
      sb.append("Store[" + storeIndex + "] is NULL");
      return sb.toString();
    }
    sb.append("Store[" + storeIndex + "] status: ").append(store.status);
    if (!store.status) {
      return sb.toString();
    }

    Page page = store.pages[pageIndex];
    if (page == null) {
      sb.append(", Page[" + pageIndex + "] is NULL");
      return sb.toString();
    }
    sb.append(", Page[" + pageIndex + "] status: ").append(page.status);
    if (!page.status) {
      return sb.toString();
    }

    sb.append("\n\n");
    int len = page.values.length;
    String hexString = MoreBytes.toHex(page.values, 0, len);

    Iterator<String> iter =
        Splitter.fixedLength(StoreConfiguration.DUMP_LINE_SIZE).split(hexString).iterator();
    while (iter.hasNext()) {
      sb.append(iter.next());
      sb.append("\n");
    }

    return sb.toString();
  }

  public StoreConfiguration getConfiguration() {
    return configuration;
  }

  public static void main(String[] args) {
    StoreConfiguration storeConfiguration = StoreConfiguration._default();
    storeConfiguration.setStore_number(16);
    storeConfiguration.setPage_number_in_store(4);
    storeConfiguration.setPage_size(2048);
    Stores stores = new Stores(storeConfiguration);

    stores.stores[0] = new Store();
    stores.stores[0].status = true;
    stores.stores[0].pages[0] = new Page();
    stores.stores[0].pages[0].status = true;

    for (int i = StoreConfiguration.PAGE_SIZE / 2; i < StoreConfiguration.PAGE_SIZE; i++) {
      stores.stores[0].pages[0].values[i] = (byte) 255;
    }

    // System.out.println(stores.dump(0, 0));

    System.out.println(Math.log(StoreConfiguration.DECAY_mttvf));
    System.out.println(StoreConfiguration.DECAY_mttvf / 1000 / 60 / 60);
    System.out.println(Math.log(StoreConfiguration.DECAY_mttsf));
    System.out.println(StoreConfiguration.DECAY_mttsf / 1000 / 60 / 60);
  }
}
