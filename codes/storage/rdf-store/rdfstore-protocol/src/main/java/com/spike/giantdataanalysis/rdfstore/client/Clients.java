package com.spike.giantdataanalysis.rdfstore.client;

import generated.filesystem.client.ProcessStructureProtos.ClientId;

/**
 * 
 */
public final class Clients {

  // TODO(zhoujiagen) dummy client id extractor
  public static ClientId getClientId() {
    return ClientId.newBuilder()//
        .setClientId(String.valueOf(Thread.currentThread().getId())).build();
  }
}
