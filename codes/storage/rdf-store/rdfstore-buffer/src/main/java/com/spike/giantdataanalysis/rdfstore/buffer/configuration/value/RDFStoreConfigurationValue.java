package com.spike.giantdataanalysis.rdfstore.buffer.configuration.value;

import com.spike.giantdataanalysis.rdfstore.configuration.value.OverridableConfigurationValue;

public class RDFStoreConfigurationValue
    implements OverridableConfigurationValue<RDFStoreConfigurationValue> {
  public RDFStoreBufferConfigurationValue buffer = new RDFStoreBufferConfigurationValue();

  @Override
  public void override(RDFStoreConfigurationValue another) {
    if (another == null) {
      return;
    }

    buffer.override(another.buffer);
  }

  @Override
  public String repr() {
    return buffer.repr();
  }
}