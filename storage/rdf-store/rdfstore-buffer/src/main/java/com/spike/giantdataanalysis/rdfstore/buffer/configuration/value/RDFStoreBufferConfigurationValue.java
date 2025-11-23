package com.spike.giantdataanalysis.rdfstore.buffer.configuration.value;

import com.spike.giantdataanalysis.rdfstore.configuration.value.OverridableConfigurationValue;

public class RDFStoreBufferConfigurationValue
    implements OverridableConfigurationValue<RDFStoreBufferConfigurationValue> {

  public Integer totalSize = 256; // MB
  public String strategy = "direct";

  @Override
  public void override(RDFStoreBufferConfigurationValue another) {
    if (another == null) {
      return;
    }

    if (another.totalSize != null) {
      this.totalSize = another.totalSize;
    }
    if (another.strategy != null) {
      this.strategy = another.strategy;
    }

  }

  @Override
  public String repr() {
    StringBuilder sb = new StringBuilder();
    sb.append("totalSize=").append(totalSize).append(System.lineSeparator());
    sb.append("strategy=").append(strategy).append(System.lineSeparator());
    return sb.toString();
  }

}
