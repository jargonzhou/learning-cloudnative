# VictoriaMetrics
- [Doc](https://victoriametrics.com/products/open-source/)
- [Code](https://github.com/VictoriaMetrics/VictoriaMetrics)

> VictoriaMetrics is a fast, cost-saving, and scalable solution for monitoring and managing time series data. It delivers high performance and reliability, making it an ideal choice for businesses of all sizes.

# Components
VictoriaMetrics ecosystem contains the following components additionally to [single-node VictoriaMetrics](https://docs.victoriametrics.com/):
- [vmagent](https://docs.victoriametrics.com/vmagent/) - lightweight agent for receiving metrics via [pull-based](https://docs.victoriametrics.com/vmagent/#how-to-collect-metrics-in-prometheus-format) and [push-based](https://docs.victoriametrics.com/vmagent/#how-to-push-data-to-vmagent) protocols, transforming and sending them to the configured Prometheus-compatible remote storage systems such as VictoriaMetrics.
- [vmalert](https://docs.victoriametrics.com/vmalert/) - a service for processing Prometheus-compatible alerting and recording rules.
- [vmalert-tool](https://docs.victoriametrics.com/vmalert-tool/) - a tool for validating alerting and recording rules.
- [vmauth](https://docs.victoriametrics.com/vmauth/) - authorization proxy and load balancer optimized for VictoriaMetrics products.
- [vmgateway](https://docs.victoriametrics.com/vmgateway/) - authorization proxy with per-[tenant](https://docs.victoriametrics.com/cluster-victoriametrics/#multitenancy) rate limiting capabilities.
- [vmctl](https://docs.victoriametrics.com/vmctl/) - a tool for migrating and copying data between different storage systems for metrics.
- [vmbackup](https://docs.victoriametrics.com/vmbackup/), [vmrestore](https://docs.victoriametrics.com/vmrestore/) and [vmbackupmanager](https://docs.victoriametrics.com/vmbackupmanager/) - tools for creating backups and restoring from backups for VictoriaMetrics data.
- `vminsert`, `vmselect` and `vmstorage` - components of [VictoriaMetrics cluster](https://docs.victoriametrics.com/cluster-victoriametrics/).
- [VictoriaLogs](https://docs.victoriametrics.com/victorialogs/) - user-friendly cost-efficient database for logs.
# Cluster
VictoriaMetrics cluster consists of the following services:
- `vmstorage` - stores the raw data and returns the queried data on the given time range for the given label filters
- `vminsert` - accepts the ingested data and spreads it among `vmstorage` nodes according to consistent hashing over metric name and all its labels
- `vmselect` - performs incoming queries by fetching the needed data from all the configured `vmstorage` nodes

![Cluster Architecture](https://docs.victoriametrics.com/victoriametrics/Cluster-VictoriaMetrics_cluster-scheme.webp)
