# Prometheus
* https://prometheus.io/
* https://github.com/prometheus/prometheus
  * Golang, React

> The Prometheus monitoring system and time series database.

books:
- Brazil, Brian. **Prometheus: Up & Running**. 2018. O’Reilly Media.


actions:
- https://github.com/jargonzhou/application-store/tree/main/ops/prometheus
- [APM.md](./APM.md)

# Architecture
This diagram illustrates the architecture of Prometheus and some of its ecosystem components:

![https://github.com/prometheus/prometheus/raw/main/documentation/images/architecture.svg](https://github.com/prometheus/prometheus/raw/main/documentation/images/architecture.svg)

Prometheus scrapes metrics from instrumented jobs, either directly or via an intermediary push gateway for short-lived jobs. It stores all scraped samples locally and runs rules over this data to either aggregate and record new time series from existing data or generate alerts. Grafana or other API consumers can be used to visualize the collected data.
# Data Model

Prometheus fundamentally stores all data as _[time series](https://en.wikipedia.org/wiki/Time_series)_: streams of timestamped values belonging to the same metric and the same set of labeled dimensions. Besides stored time series, Prometheus may generate temporary derived time series as the result of queries.
# Querying Prometheus

Prometheus provides a functional query language called **PromQL (Prometheus Query Language)** that lets the user select and aggregate time series data in real time. The result of an expression can either be shown as a graph, viewed as tabular data in Prometheus's expression browser, or consumed by external systems via the [HTTP API](https://prometheus.io/docs/prometheus/latest/querying/api/).

# Storage

Prometheus includes a local on-disk time series database, but also optionally integrates with remote storage systems.

- TSDB format: https://github.com/prometheus/prometheus/blob/release-2.47/tsdb/docs/format/README.md
# Visualization

- [Grafana.ipynb](./Grafana.ipynb)
# Instrumenting

- Pushing Metrics: https://prometheus.io/docs/instrumenting/pushing/
- Exporters and Intergration: https://prometheus.io/docs/instrumenting/exporters/
# Altering

- Altering Overview: https://prometheus.io/docs/alerting/latest/overview/
- [Prometheus的alertmanager使用](https://www.liuvv.com/p/6df22f03.html)
- [A simple echo server to inspect http web requests](https://gist.github.com/huyng/814831)
	- [Webhook.site](https://webhook.site/)
- [Observability Metrics: Prometheus, Grafana, Alert Manager & Slack Notifications](https://medium.com/cloud-native-daily/chapter-9-observability-metrics-prometheus-grafana-alert-manager-slack-notifications-b5dcac31b462)
