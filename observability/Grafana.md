# Grafana
- tutorial: [Get started with Grafana and Prometheus](https://grafana.com/docs/grafana/latest/getting-started/get-started-grafana-prometheus)
- [Introduction to Metrics, Logs, Traces and Profiles in Grafana](https://github.com/grafana/intro-to-mltp)
  - Grafana
  - Mimir: a backend store for metrics data
  - Loki: a backend store for long-term log retention
  - Temp: a backend store for longterm trace retention
  - Pyroscope: a continuous profiling backend store
  - k6: a load testing suite to load and monitor your application
  - Beyla: an eBPF-based tool for generating metrics and trace data without application instrumentation
  - Alloy: a Grafana distribution of the OpenTelemetry collector, receiving MLTP(metrics/logs/traces/profiles) and forwarding to relevant database stores.

actions:
- https://github.com/jargonzhou/application-store/tree/main/ops/grafana
- https://github.com/jargonzhou/application-store/tree/main/ops/prometheus
- workbench\Java\JavaEE\example-springcloud\admin\grafana

# Dashboards
- https://grafana.com/docs/grafana/latest/dashboards/
- full library of dashboards: https://grafana.com/grafana/dashboards/?pg=docs-grafana-latest-dashboards


# HTTP API
* https://grafana.com/docs/grafana/latest/developers/http_api/

# Data sources

```shell
curl -s -u admin:devops+admin http://localhost:13000/api/datasources
```

```python
import pprint
import requests
import base64
url = 'http://localhost:13000/api/datasources'
headers = {}
params = {}
data = {
}

headers['Authorization'] = 'Basic ' + \
    base64.b64encode('admin:devops+admin'.encode('utf-8')).decode('utf-8')
print(url)
pprint.pprint(headers)
pprint.pprint(params)

r = requests.get(url=url, headers=headers, params=params, verify=False)
print(r.json())
```

# Loki
* https://grafana.com/docs/loki/latest/
* example: https://github.com/grafana/loki/tree/main/examples
* [Loki4j Logback](https://loki4j.github.io/loki-logback-appender/): Pure Java Logback appender for Grafana Loki

> Grafana Loki is a set of open source components that can be composed into a fully featured logging stack. A small index and highly compressed chunks simplifies the operation and significantly lowers the cost of Loki.

# Tempo
* https://grafana.com/docs/tempo/latest/
* example: https://github.com/grafana/tempo/tree/main/example

> Grafana Tempo is an open-source, easy-to-use, and high-scale distributed tracing backend. Tempo lets you search for traces, generate metrics from spans, and link your tracing data with logs and metrics.
tracing pipeline components:
- client instrumentation
  - OpenTelemetry
  - Zipkin
- pipeline(optional): Alloy
- backend: Tempo
- visualization: Grafana
  - Traces Drilldown: RED(Rate, Errors, Duration)
  - TraceQL
  - link traces and logs/metrics/profiles

traces:
- span and resource attributes: with naming conventions
- where to add spans
- span length

setup:
- Docker Compose
- Helm: on Kubernetes
- Tanka: on Kunbernetes

# Problems
- [[kube-prometheus-stack] grafana: Readiness probe failed: connect: connection refused](https://github.com/prometheus-community/helm-charts/issues/4251): `grafana.containerSecurityContext.readOnlyRootFilesystem: false`
