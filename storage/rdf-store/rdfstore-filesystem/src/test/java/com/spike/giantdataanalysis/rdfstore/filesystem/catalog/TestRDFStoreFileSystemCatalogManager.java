package com.spike.giantdataanalysis.rdfstore.filesystem.catalog;

import org.junit.BeforeClass;
import org.junit.Test;

import com.spike.giantdataanalysis.rdfstore.filesystem.configuration.RDFStoreFileSystemConfiguration;
import com.spike.giantdataanalysis.rdfstore.metric.RDFStoreMetricRegistry;

public class TestRDFStoreFileSystemCatalogManager {

  private static RDFStoreFileSystemCatalogManager catalogManager;
  private static RDFStoreMetricRegistry metricRegistry;

  @BeforeClass
  public static void setUpBeforeClass() {
    metricRegistry = new RDFStoreMetricRegistry();
    catalogManager = new RDFStoreFileSystemCatalogManager(//
        RDFStoreFileSystemConfiguration.configuration, metricRegistry);

    metricRegistry.initializeJmxReporter();
    metricRegistry.startJmxReporter();
  }

  @Test
  public void testPersistent() {
    String fileName = "graph1.db";
    catalogManager.createRegularFileNo(fileName);
    fileName = "graph2.db";
    catalogManager.createRegularFileNo(fileName);
  }

  @Test
  public void testRestore() {
    catalogManager.restore();
  }
}
