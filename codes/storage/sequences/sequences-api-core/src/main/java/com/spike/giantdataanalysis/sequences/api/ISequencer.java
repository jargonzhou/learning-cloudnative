package com.spike.giantdataanalysis.sequences.api;

import java.io.Serializable;

import com.spike.giantdataanalysis.sequences.api.domain.Sequence;
import com.spike.giantdataanalysis.sequences.api.domain.SequenceGroup;

public interface ISequencer extends Serializable {

  long START_VALUE = 1L;

  void init(String name, long value);

  Sequence current(String name);

  Sequence next(String name);

  SequenceGroup next(String name, int n);
}
