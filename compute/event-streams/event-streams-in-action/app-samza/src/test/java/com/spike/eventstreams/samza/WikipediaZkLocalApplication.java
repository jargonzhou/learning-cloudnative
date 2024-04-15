/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spike.eventstreams.samza;

import joptsimple.OptionSet;
import org.apache.samza.config.Config;
import org.apache.samza.runtime.LocalApplicationRunner;
import org.apache.samza.util.CommandLine;

// https://samza.apache.org/learn/documentation/latest/deployment/standalone.html
// ./deploy/samza/bin/run-class.sh \
// samza.examples.wikipedia.application.WikipediaZkLocalApplication  \
// --config job.config.loader.factory=org.apache.samza.config.loaders.PropertiesConfigLoaderFactory \
// --config job.config.loader.properties.path=app-samza/src/test/resources/wikipedia-application-local-runner.properties

/**
 * An entry point for {@link WikipediaApplication} that runs in stand alone mode using zookeeper.
 * It waits for the job to finish; The job can also be ended by killing this process.
 */
public class WikipediaZkLocalApplication {

  /**
   * Executes the application using the local application runner.
   * It takes two required command line arguments
   *  --config job.config.loader.factory: a fully {@link org.apache.samza.config.loaders.PropertiesConfigLoaderFactory} class name
   *  --config job.config.loader.properties.path: path to application properties
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    System.out.println(System.getProperty("user.dir"));

    CommandLine cmdLine = new CommandLine();
    OptionSet options = cmdLine.parser().parse(args);
    Config config = cmdLine.loadConfig(options);

    WikipediaApplication app = new WikipediaApplication();
    LocalApplicationRunner runner = new LocalApplicationRunner(app, config);
    runner.run();
    runner.waitForFinish();
  }
}
