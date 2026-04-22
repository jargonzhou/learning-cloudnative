package com.spike.giantdataanalysis.sequences.faultmodel.store;

class Store {
  boolean status = false;
  Page[] pages = new Page[StoreConfiguration.PAGE_NUMBER_IN_STORE];

  // Page get(int pageIndex) {
  // if (pageIndex < 0 || pageIndex > Configuration.PAGE_NUMBER_IN_STORE) return null;
  //
  // return pages[pageIndex];
  // }
  //
  // Page getCreate(int pageIndex) {
  // if (pageIndex < 0 || pageIndex > Configuration.PAGE_NUMBER_IN_STORE) return null;
  //
  // Page result = pages[pageIndex];
  // if (result == null) {
  // result = new Page();
  // result.status = true;
  // result.updateVersion();
  // pages[pageIndex] = result;
  // }
  //
  // return result;
  // }

}