# Apache Flink
* https://flink.apache.org

> Stateful Computations over Data Streams
>
> Apache Flink is a framework and distributed processing engine for stateful computations over unbounded and bounded data streams. Flink has been designed to run in all common cluster environments, perform computations at in-memory speed and at any scale.

LTS version: 1.20.

Doc
- **Try Flink**
  - First steps
  - Fraud Detection with the DataStream API
  - Real Time Reporting with the Table API
  - Flink Operations Playground
- **Learn Flink**
  - Overview
  - Intro to the DataStream API
  - Data Pipelines & ETL
  - Streaming Analytics
  - Event-driven Applications
  - Fault Tolerance
- **Concepts**
  - Overview
  - Stateful Stream Processing
  - Timely Stream Processing
  - Flink Architecture
  - Glossary
- **Application Development**
  - Project Configuration
  - DataStream API
  - Table API & SQL
  - Python API
- **Libraries**
  - Event Processing (CEP)
  - State Processor API
- **Connectors**
  - DataStream Connectors
  - Table API Connectors
- **Deployment**
  - Overview
  - Java Compatibility
  - Resource Providers
  - Configuration
  - Memory Configuration
  - Command-Line Interface
  - Elastic Scaling
  - Fine-Grained Resource Management
  - Speculative Execution
  - File Systems
  - High Availability
  - Metric Reporters
  - Trace Reporters
  - REPLs
  - Security
  - Advanced
- **Operations**
  - State & Fault Tolerance
  - Metrics
  - Traces
  - REST API
  - Batch
  - Debugging
  - Monitoring
  - Upgrading Applications and Flink Versions
  - Production Readiness Checklist
- **Flink Development**
  - Importing Flink into an IDE
  - Building Flink from Source
- **Internals**
  - Jobs and Scheduling
  - Task Lifecycle
  - File Systems

# Deployment

Components:
- Flink Client
	- Command line interface
	- REST endpoint
	- SQL client
	- Python REPL
- JobManager
	- Standalone
	- Kubernetes
	- YARN
- TaskManager
- External components
	- High Available Service Provider: Zookeeper, Kubernetes HA
	- File Storage and Persistency
	- Resource Provider
	- Metrics Storage
	- Application-level data sources and sinks: connectors

# RTFSC

## Data Structures

(1) `DataStream`

read: `StreamExecutionEnvironment`

transform

write

`SideOutput`, `OutputTag`

(2) environment

`StreamExecutionEnvironment`, `Environment`, `RuntimeContext`

(3) `StreamElement`

`StreamRecord`, `LatencyMarker`, `Watermark`, `StreamStatus`

(4) `Transformation`

`PhysicalTransformation`

(5) `StreamOperator`

flink-streaming-java, flink-table-runtime-blink

(6) `Function`

`SourceFunction`, `SinkFunction`, `Function`

`RichFunction`, `ProcessFunction`

`BroadcaseProcessFunction`

`AsyncFunction`

`CheckpointedFunction`, `ListCheckpointed`

(7) `Partition`

`StreamPartitioner`, `ChannelSelector`

(8) Connector

Flink-connectors module, Bahir

(9) `AbstractID`

(10) State

(11) Dataflow

- Jobs and Scheduling: https://nightlies.apache.org/flink/flink-docs-release-1.16/docs/internals/job_scheduling/

`StreamGraph`, `JobGraph`, `ExecutionGraph`

# See Also
- [Remove Gelly](https://nightlies.apache.org/flink/flink-docs-stable/release-notes/flink-1.17/#remove-gelly): Gelly has been removed from Flink. Current users of Gelly should not upgrade to Flink 1.17 but stay on an older version.
- Kubernetes Operator
- CDC
- Agents
- ML