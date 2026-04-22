package com.spike.giantdataanalysis.rdfstore.buffer;

import com.spike.giantdataanalysis.rdfstore.buffer.configuration.RDFStoreBufferConfiguration;
import com.spike.giantdataanalysis.rdfstore.buffer.strategy.RDFStoreBufferStrategy;
import com.spike.giantdataanalysis.rdfstore.buffer.strategy.RDFStoreDirectBufferStrategy;
import com.spike.giantdataanalysis.rdfstore.buffer.strategy.RDFStoreVMBufferStrategy;
import com.spike.giantdataanalysis.rdfstore.buffer.strategy.RDFStoreBufferStrategy.BufferStrategyEnum;
import com.spike.giantdataanalysis.rdfstore.constant.RDFStoreConstant;
import com.spike.giantdataanalysis.rdfstore.filesystem.RDFStoreFileSystem;
import com.spike.giantdataanalysis.rdfstore.metric.RDFStoreMetricRegistry;

/**
 * Buffer Manager.
 */
public final class RDFStoreBufferManager {

  private final RDFStoreBufferConfiguration configuration;
  private final RDFStoreFileSystem fs;
  private final RDFStoreBufferStrategy strategy;
  private final RDFStoreMetricRegistry metricRegistry;

  private final int totalSizeInBytes;

  public RDFStoreBufferManager(RDFStoreBufferConfiguration configuration,
      RDFStoreFileSystem fileSystem, RDFStoreMetricRegistry metricRegistry) {
    this.configuration = configuration;
    this.fs = fileSystem;
    this.totalSizeInBytes = configuration.rdfStore.buffer.totalSize * RDFStoreConstant.M;

    BufferStrategyEnum bse = BufferStrategyEnum.get(configuration.rdfStore.buffer.strategy);
    switch (bse) {
    case VM:
      this.strategy = new RDFStoreVMBufferStrategy();
      break;

    default:
      this.strategy = new RDFStoreDirectBufferStrategy(this.fs, this.totalSizeInBytes);
      break;
    }

    this.metricRegistry = metricRegistry;
  }

  // ---------------------------------------------------------------------------
  // Initialize
  // ---------------------------------------------------------------------------

  public void initialize() {
    this.strategy.initializeSpace(totalSizeInBytes);
  }

  // ---------------------------------------------------------------------------
  // Manipulation: fix, flush
  // ---------------------------------------------------------------------------
  // Boolean bufferfix(PAGEID pageid, LOCK_MODE mode, BUFFER_ACC_CBP * address)
  // Boolean bufferunfix(BUFFER_ACC_CBP)
  // Boolean emptyfix(PAGEID pageid, LOCK_MODE mode, BUFFER_ACC_CBP address)
  // Boolean flush(BUFFER_ACC_CBP)
  // Uint hashpage(PAGEID)
  // FILEID find_filecb(PAGEID)
  // LSN log_max_lsn()

  // ---------------------------------------------------------------------------
  // BACB: block access control block management
  // ---------------------------------------------------------------------------

  // BUFFER_ACC_CBP get_acb()
  // void give_acb(BUFFER_ACC_CBP)

  // ---------------------------------------------------------------------------
  // BCB: block control block management
  // ---------------------------------------------------------------------------
  // BUFFER_CBP get_bcb()
  // void give_bcb(BUFFER_CBP)
}
