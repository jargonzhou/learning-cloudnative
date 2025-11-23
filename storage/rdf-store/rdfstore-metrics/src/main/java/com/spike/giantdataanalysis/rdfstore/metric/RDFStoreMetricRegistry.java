package com.spike.giantdataanalysis.rdfstore.metric;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricRegistry.MetricSupplier;
import com.codahale.metrics.MetricRegistryListener;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.HealthCheckRegistryListener;
import com.codahale.metrics.jmx.JmxReporter;

/**
 * Metric Store in RDF Store.
 */
public class RDFStoreMetricRegistry {

  private final MetricRegistry metricRegistry;
  private HealthCheckRegistry healthCheckRegistry;
  private JmxReporter jmxReporter;

  public RDFStoreMetricRegistry() {
    this.metricRegistry = new MetricRegistry();
  }

  public RDFStoreMetricRegistry(MetricRegistry metricRegistry) {
    this.metricRegistry = metricRegistry;
  }

  // ---------------------------------------------------------------------------
  // MetricRegistry: Meter, Guage, Counter, Histogram, Timer
  // ---------------------------------------------------------------------------
  public MetricRegistry getMetricRegistry() {
    return metricRegistry;
  }

  public <T extends Metric> T register(String name, T metric) {
    return metricRegistry.register(name, metric);
  }

  public Meter meter(String name) {
    return metricRegistry.meter(name);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public <T> Gauge<T> gauge(String name, final MetricSupplier<Gauge> supplier) {
    return metricRegistry.gauge(name, supplier);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Gauge<Long> gauge(String name, final long value) {
    return metricRegistry.gauge(name, new MetricSupplier<Gauge>() {
      @Override
      public Gauge<Long> newMetric() {
        return new Gauge<Long>() {

          @Override
          public Long getValue() {
            return value;
          }
        };
      }
    });
  }

  public Counter counter(String name) {
    return metricRegistry.counter(name);
  }

  public Histogram histogram(String name) {
    return metricRegistry.histogram(name);
  }

  public Timer timer(String name) {
    return metricRegistry.timer(name);
  }

  // ---------------------------------------------------------------------------
  // HealthCheckRegistry
  // ---------------------------------------------------------------------------
  public HealthCheckRegistry getHealthCheckRegistry() {
    return healthCheckRegistry;
  }

  public void setHealthCheckRegistry(HealthCheckRegistry healthCheckRegistry) {
    this.healthCheckRegistry = healthCheckRegistry;
  }

  public void healthCheck(String name, HealthCheck healthCheck) {
    healthCheckRegistry.register(name, healthCheck);
  }

  // TODO(zhoujiagen) connection health checker
  // REF: https://metrics.dropwizard.io/4.0.0/getting-started.html#health-checks

  // ---------------------------------------------------------------------------
  // listener
  // ---------------------------------------------------------------------------

  public void addListener(MetricRegistryListener listener) {
    metricRegistry.addListener(listener);
  }

  public void removeListener(MetricRegistryListener listener) {
    metricRegistry.removeListener(listener);
  }

  public void addListener(HealthCheckRegistryListener listener) {
    healthCheckRegistry.addListener(listener);
  }

  public void removeListener(HealthCheckRegistryListener listener) {
    healthCheckRegistry.removeListener(listener);
  }

  // ---------------------------------------------------------------------------
  // reporter
  // ---------------------------------------------------------------------------
  public JmxReporter getJmxReporter() {
    return jmxReporter;
  }

  public void initializeJmxReporter() {
    this.jmxReporter = JmxReporter.forRegistry(this.metricRegistry).build();
  }

  public void startJmxReporter() {
    if (this.jmxReporter == null) {
      this.initializeJmxReporter();
    }

    this.jmxReporter.start();
  }

}
