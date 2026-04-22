package com.spike.giantdataanalysis.rdfstore.buffer.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.spike.giantdataanalysis.rdfstore.buffer.configuration.value.RDFStoreConfigurationValue;

/**
 * Buffer Configuration.
 */
public final class RDFStoreBufferConfiguration {
  private static final Logger LOG = LoggerFactory.getLogger(RDFStoreBufferConfiguration.class);

  // ---------------------------------------------------------------------------
  // Set up
  // ---------------------------------------------------------------------------

  public static String DEFAULT_FILE_NAME = "conf/rdfstore-buffer-default.yaml";
  public static String FILE_NAME = "conf/rdfstore-buffer.yaml";
  private static Yaml yaml = new Yaml(new Constructor(RDFStoreBufferConfiguration.class));

  public static RDFStoreBufferConfiguration configuration = null;

  private RDFStoreBufferConfiguration() {
  }

  static {
    FileReader reader = null;
    try {
      reader = new FileReader(DEFAULT_FILE_NAME);
      configuration = yaml.<RDFStoreBufferConfiguration> load(reader);
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
    RDFStoreBufferConfiguration overrideResult = null;
    FileReader reader = null;
    try {
      Path filePath = Paths.get(FILE_NAME);
      if (Files.exists(filePath)) {
        reader = new FileReader(FILE_NAME);
        overrideResult = yaml.<RDFStoreBufferConfiguration> load(reader);
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

  }

  // ---------------------------------------------------------------------------
  // Properties
  // ---------------------------------------------------------------------------
  public RDFStoreConfigurationValue rdfStore = new RDFStoreConfigurationValue();

  public String dump() {
    return rdfStore.repr();
  }
}
