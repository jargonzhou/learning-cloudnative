package com.spike.giantdataanalysis.rdfstore.configuration.value;

public interface OverridableConfigurationValue<T> {
  void override(T another);

  String repr();
}