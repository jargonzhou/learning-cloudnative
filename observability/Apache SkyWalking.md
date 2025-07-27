# Apache SkyWalking
* https://skywalking.apache.org/

SkyWalking is logically split into four parts: Probes, Platform backend, Storage and UI.

![https://skywalking.apache.org/images/home/architecture_2160x720.png?t=20220617](https://skywalking.apache.org/images/home/architecture_2160x720.png?t=20220617)

- **Probe**s collect telemetry data, including metrics, traces, logs and events in various formats(SkyWalking, Zipkin, OpenTelemetry, Prometheus, Zabbix, etc.)
- **Platform backend** supports data aggregation, analysis and streaming process covers traces, metrics, logs and events. Work as Aggregator Role, Receiver Role or both.
- **Storage** houses SkyWalking data through an open/plugable interface. You can choose an existing implementation, such as ElasticSearch, H2, MySQL, TiDB, BanyanDB, or implement your own.
- **UI** is a highly customizable web based interface allowing SkyWalking end users to visualize and manage SkyWalking data.


actions:
- https://github.com/jargonzhou/application-store/tree/main/ops/skywalking
- workbench\Java\JavaEE\example-springcloud\admin\skywalking

# Agents

- language based native agent: `-javaagent`
- service mesh probes: sidecar, control plane, proxy
- 3rd-party instrument library: Zipkin
- eBPF agent

# OAP: Observability Analysis Platform

# Capabilities

- tracing: Zipkin v1/v2, Jaeger
- metrics: Istio, Envoy, Linkerd
- logging: disk, network

# languages engine

- Observability Analysis Language: OAL
- Meter Analysis Language: MAL
- Log Analysis Language: LAL

# Profiling

- in-process: auto-instrument agents
- out-of-process: eBPF agent
- continuous: eBPF agent

# Setup

- Tracing
- Metrics
- Logging
- Profiling
- Backend setup
- UI setup
- …
# API

## Telemetry APIs/Protocols

- Tracing: Trace Data Protocol, Cross Process Propagation/Correlation Headers Protocol
- Metrics APIs: Meter APIs, Browser Protocol, JVM Metrics APIs
- Log Data Protocol
- Service Instance Properties APIs
- Events Report Protocol
- Profiling APIs

## Query APIs

- GraphQL APIs: Query Protocol, MQE(Metrics Query Expression)
- PromQL(PromQL Query Language) APIs
- LogQL(Log Query Lanaguage) APIs