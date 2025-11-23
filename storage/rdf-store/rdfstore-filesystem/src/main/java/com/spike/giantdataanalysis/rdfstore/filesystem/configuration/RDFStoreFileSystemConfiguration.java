package com.spike.giantdataanalysis.rdfstore.filesystem.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.spike.giantdataanalysis.rdfstore.filesystem.configuration.value.RDFStoreConfigurationValue;
import com.spike.giantdataanalysis.rdfstore.filesystem.exception.RDFStoreFileSystemCatalogException;
import com.spike.giantdataanalysis.rdfstore.filesystem.exception.RDFStoreFileSystemException;

/**
 * Configuration of RDF Store File System.
 */
public final class RDFStoreFileSystemConfiguration {
  private static final Logger LOG = LoggerFactory.getLogger(RDFStoreFileSystemConfiguration.class);

  // ---------------------------------------------------------------------------
  // Set up
  // ---------------------------------------------------------------------------

  public static String DEFAULT_FILE_NAME = "conf/rdfstore-filesystem-default.yaml";
  public static String FILE_NAME = "conf/rdfstore-filesystem.yaml";
  private static Yaml yaml = new Yaml(new Constructor(RDFStoreFileSystemConfiguration.class));

  public static RDFStoreFileSystemConfiguration configuration = null;

  private RDFStoreFileSystemConfiguration() {
  }

  static {
    FileReader reader = null;
    try {
      reader = new FileReader(DEFAULT_FILE_NAME);
      configuration = yaml.<RDFStoreFileSystemConfiguration> load(reader);
    } catch (Exception e) {
      LOG.error("Load configuration file " + DEFAULT_FILE_NAME + " failed!", e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }

    override();
    bootstrapFacility();
  }

  public static void override() {
    RDFStoreFileSystemConfiguration overrideResult = null;
    FileReader reader = null;
    try {
      Path filePath = Paths.get(FILE_NAME);
      if (Files.exists(filePath)) {
        reader = new FileReader(FILE_NAME);
        overrideResult = yaml.<RDFStoreFileSystemConfiguration> load(reader);
        configuration.rdfStore.override(overrideResult.rdfStore);
      }
    } catch (Exception e) {
      LOG.error("Load configuration file " + FILE_NAME + " failed!", e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  public static void bootstrapFacility() {
    Path root = Paths.get(configuration.rdfStore.fileSystem.root);
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        throw RDFStoreFileSystemException.newE(e);
      }
    }

    Path catalogRoot = Paths.get(configuration.rdfStore.fileSystem.catalogRoot);
    if (!Files.exists(catalogRoot)) {
      try {
        Files.createDirectories(catalogRoot);
      } catch (IOException e) {
        throw RDFStoreFileSystemCatalogException.newE(e);
      }
    }
  }

  // ---------------------------------------------------------------------------
  // Properties
  // ---------------------------------------------------------------------------
  public RDFStoreConfigurationValue rdfStore = new RDFStoreConfigurationValue();

  public String dump() {
    return rdfStore.repr();
  }
}
