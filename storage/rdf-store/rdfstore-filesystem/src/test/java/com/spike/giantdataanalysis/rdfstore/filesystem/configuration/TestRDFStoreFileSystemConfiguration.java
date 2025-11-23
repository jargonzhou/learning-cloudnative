package com.spike.giantdataanalysis.rdfstore.filesystem.configuration;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 */
public class TestRDFStoreFileSystemConfiguration {
  @Test
  public void testConfiguration() {
    RDFStoreFileSystemConfiguration configuration = RDFStoreFileSystemConfiguration.configuration;
    Assert.assertNotNull(configuration.rdfStore.fileSystem.root);
    Assert.assertNotEquals(configuration.rdfStore.fileSystem.root, "/tmp");
    System.out.println(configuration.dump());
  }
}
