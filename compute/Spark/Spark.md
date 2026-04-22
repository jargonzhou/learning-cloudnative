# Apache Spark
* https://spark.apache.org
* https://github.com/apache/spark

> Apache Spark is a unified analytics engine for large-scale data processing. It provides high-level APIs in Java, Scala, Python and R, and an optimized engine that supports general execution graphs. It also supports a rich set of higher-level tools including Spark SQL for SQL and structured data processing, pandas API on Spark for pandas workloads, MLlib for machine learning, GraphX for graph processing, and Structured Streaming for incremental computation and stream processing.

**Programming Guides:**

- [Quick Start](https://spark.apache.org/docs/latest/quick-start.html): a quick introduction to the Spark API; start here!
- [RDD Programming Guide](https://spark.apache.org/docs/latest/rdd-programming-guide.html): overview of Spark basics - RDDs (core but old API), accumulators, and broadcast variables
- [Spark SQL, Datasets, and DataFrames](https://spark.apache.org/docs/latest/sql-programming-guide.html): processing structured data with relational queries (newer API than RDDs)
- [Structured Streaming](https://spark.apache.org/docs/latest/streaming/index.html): processing structured data streams with relation queries (using Datasets and DataFrames, newer API than DStreams)
- [Spark Streaming](https://spark.apache.org/docs/latest/streaming-programming-guide.html): processing data streams using DStreams (old API)
- [MLlib](https://spark.apache.org/docs/latest/ml-guide.html): applying machine learning algorithms
- [GraphX](https://spark.apache.org/docs/latest/graphx-programming-guide.html): processing graphs
- [SparkR (Deprecated)](https://spark.apache.org/docs/latest/sparkr.html): processing data with Spark in R
- [PySpark](https://spark.apache.org/docs/latest/api/python/getting_started/index.html): processing data with Spark in Python
- [Spark SQL CLI](https://spark.apache.org/docs/latest/sql-distributed-sql-engine-spark-sql-cli.html): processing data with SQL on the command line
- [Declarative Pipelines](https://spark.apache.org/docs/latest/declarative-pipelines-programming-guide.html): building data pipelines that create and maintain multiple tables

**Deployment Guides:**

- [Cluster Overview](https://spark.apache.org/docs/latest/cluster-overview.html): overview of concepts and components when running on a cluster
- [Submitting Applications](https://spark.apache.org/docs/latest/submitting-applications.html): packaging and deploying applications
- Deployment modes:
    - [Standalone Deploy Mode](https://spark.apache.org/docs/latest/spark-standalone.html): launch a standalone cluster quickly without a third-party cluster manager
    - [YARN](https://spark.apache.org/docs/latest/running-on-yarn.html): deploy Spark on top of Hadoop NextGen (YARN)
    - [Kubernetes](https://spark.apache.org/docs/latest/running-on-kubernetes.html): deploy Spark apps on top of Kubernetes directly
    - [Amazon EC2](https://github.com/amplab/spark-ec2): scripts that let you launch a cluster on EC2 in about 5 minutes
- [Spark Kubernetes Operator](https://github.com/apache/spark-kubernetes-operator):
    - [SparkApp](https://github.com/apache/spark-kubernetes-operator/blob/main/examples/pyspark-pi.yaml): deploy Spark apps on top of Kubernetes via [operator patterns](https://kubernetes.io/docs/concepts/extend-kubernetes/operator/)
    - [SparkCluster](https://github.com/apache/spark-kubernetes-operator/blob/main/examples/cluster-with-template.yaml): deploy Spark clusters on top of Kubernetes via [operator patterns](https://kubernetes.io/docs/concepts/extend-kubernetes/operator/)

**Other Documents:**

- [Configuration](https://spark.apache.org/docs/latest/configuration.html): customize Spark via its configuration system
- [Monitoring](https://spark.apache.org/docs/latest/monitoring.html): track the behavior of your applications
- [Web UI](https://spark.apache.org/docs/latest/web-ui.html): view useful information about your applications
- [Tuning Guide](https://spark.apache.org/docs/latest/tuning.html): best practices to optimize performance and memory use
- [Job Scheduling](https://spark.apache.org/docs/latest/job-scheduling.html): scheduling resources across and within Spark applications
- [Security](https://spark.apache.org/docs/latest/security.html): Spark security support
- [Hardware Provisioning](https://spark.apache.org/docs/latest/hardware-provisioning.html): recommendations for cluster hardware
- Integration with other storage systems:
    - [Cloud Infrastructures](https://spark.apache.org/docs/latest/cloud-integration.html)
    - [OpenStack Swift](https://spark.apache.org/docs/latest/storage-openstack-swift.html)
- [Migration Guide](https://spark.apache.org/docs/latest/migration-guide.html): migration guides for Spark components
- [Building Spark](https://spark.apache.org/docs/latest/building-spark.html): build Spark using the Maven system
- [Contributing to Spark](https://spark.apache.org/contributing.html)
- [Third Party Projects](https://spark.apache.org/third-party-projects.html): related third party Spark projects

**External Resources:**

- [Spark Homepage](https://spark.apache.org/)
- [Spark Community](https://spark.apache.org/community.html) resources, including local meetups
- [StackOverflow tag `apache-spark`](http://stackoverflow.com/questions/tagged/apache-spark)
- [Mailing Lists](https://spark.apache.org/mailing-lists.html): ask questions about Spark here
- AMP Camps: a series of training camps at UC Berkeley that featured talks and exercises about Spark, Spark Streaming, Mesos, and more. [Videos](https://www.youtube.com/user/BerkeleyAMPLab/search?query=amp%20camp), are available online for free.
- [Code Examples](https://spark.apache.org/examples.html): more are also available in the `examples` subfolder of Spark ([Python](https://github.com/apache/spark/tree/master/examples/src/main/python), [Scala](https://github.com/apache/spark/tree/master/examples/src/main/scala/org/apache/spark/examples), [Java](https://github.com/apache/spark/tree/master/examples/src/main/java/org/apache/spark/examples), [R](https://github.com/apache/spark/tree/master/examples/src/main/r))

# Programming - RDD
# Programming - Spark SQL
# Programming - Structured Streaming
# Programming - Spark Streaming
# Programming - MLlib

MLlib is Spark’s machine learning (ML) library. Its goal is to make practical machine learning scalable and easy. At a high level, it provides tools such as:
- ML Algorithms: common learning algorithms such as classification, regression, clustering, and collaborative filtering
- Featurization: feature extraction, transformation, dimensionality reduction, and selection
- Pipelines: tools for constructing, evaluating, and tuning ML Pipelines
- Persistence: saving and load algorithms, models, and Pipelines
- Utilities: linear algebra, statistics, data handling, etc.

As of Spark 2.0, the RDD-based APIs in the `spark.mllib` package have entered maintenance mode. The primary Machine Learning API for Spark is now the DataFrame-based API in the `spark.ml` package.

API
- Pipeline APIs/流水线API
- Parameters/参数
- Feature/特征
- Classification/分类
- Clustering/聚类
- Functions/函数
- Vector and Matrix/向量和矩阵
- Recommendation/推荐
- Regression/回归
- Statistics/统计
- Tuning/调优
- Evaluation/评估
- Frequency Pattern Mining/频率模式挖掘
- Image/图片
- Distributor/分布式训练: PyTorch
- Utilities/工具

# Programming - GraphX

GraphX is currently in a maintained but inactive development state within the Apache Spark project. While it remains part of the official Spark release and receives updates to ensure compatibility with new Spark versions, there is no active development of new features or significant improvements. - [[DISCUSS] Deprecate GraphX OR Find new maintainers interested in GraphX OR leave it as is?](https://lists.apache.org/thread/qrvo6xrt8zvp5ss73z5spt9q89r0htwo)

Alternative
- GraphFrames

# Programming - PySpark

API
- Spark SQL
- Pandas API on Spark
- Structured Streaming
- MLlib (DataFrame-based)
- Spark Streaming (Legacy)
- MLlib (RDD-based)
- Spark Core
- PySpark Pipelines
- Resource Management
- Errors
- Logger
- Testing

## pandas API on Spark
* https://spark.apache.org/pandas-on-spark/

pandas on Spark can be much faster than pandas and offers syntax that is familiar to pandas users. It offers the power of Spark with the familiarity of pandas.

Here are the main advantages of pandas on Spark:
- Faster query execution on single-machine workloads (because pandas on Spark uses all available cores, processes queries in parallel, and optimizes queries)
- pandas on Spark is scalable to multiple machines in a cluster and can process big data
- pandas on Spark allows queries to be run on larger than memory datasets
- pandas on Spark provides familiar syntax for pandas users

# Programming - Declarative Pipelines

The Spark Declarative Pipelines component (part of the larger Lakeflow suite) was officially introduced into open-source Apache Spark as part of the Spark 4.1.0 release on April 9, 2026.

Spark Declarative Pipelines (SDP) is a declarative framework for building reliable, maintainable, and testable data pipelines on Spark. SDP simplifies ETL development by allowing you to focus on the transformations you want to apply to your data, rather than the mechanics of pipeline execution.

SDP is designed for both batch and streaming data processing, supporting common use cases such as:
- Data ingestion from cloud storage (Amazon S3, Azure ADLS Gen2, Google Cloud Storage)
- Data ingestion from message buses (Apache Kafka, Amazon Kinesis, Google Pub/Sub, Azure EventHub)
- Incremental batch and streaming transformations

The key advantage of SDP is its declarative approach - you define what tables should exist and what their contents should be, and SDP handles the orchestration, compute management, and error handling automatically.

![](https://spark.apache.org/docs/latest/img/declarative-pipelines-dataflow-graph.png)

```shell
pip install pyspark[pipelines]
```

# API
- [Spark Python API (Sphinx)](https://spark.apache.org/docs/latest/api/python/index.html)
- [Spark Scala API (Scaladoc)](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/index.html)
- [Spark Java API (Javadoc)](https://spark.apache.org/docs/latest/api/java/index.html)
- [Spark R API (Roxygen2)](https://spark.apache.org/docs/latest/api/R/index.html)
- [Spark SQL, Built-in Functions (MkDocs)](https://spark.apache.org/docs/latest/api/sql/index.html)

## Scala API
* https://spark.apache.org/docs/latest/api/scala/org/apache/spark/index.html

# Deployment

Cluster Mode Components

![](https://spark.apache.org/docs/latest/img/cluster-overview.png)

Terms
- **Application/应用**: User program built on Spark. Consists of a driver program and executors on the cluster.
- **Application jar/应用JAR包**: A jar containing the user's Spark application. In some cases users will want to create an "uber jar" containing their application along with its dependencies. The user's jar should never include Hadoop or Spark libraries, however, these will be added at runtime.
- **Driver program/驱动程序**: The process running the `main()` function of the application and creating the `SparkContext`
- **Cluster manager/集群管理器**: An external service for acquiring resources on the cluster (e.g. standalone manager, YARN, Kubernetes)
- **Deploy mode/部署模式**: Distinguishes where the driver process runs.
  - In **"cluster" mode/集群模式**, the framework launches the driver inside of the cluster. 
  - In **"client" mode/客户端模式**, the submitter launches the driver outside of the cluster.
- **Worker node/工作者节点**: Any node that can run application code in the cluster
- **Executor/执行器**: A process launched for an application on a worker node, that runs tasks and keeps data in memory or disk storage across them. Each application has its own executors.
- **Task/任务**: A unit of work that will be sent to one executor
- **Job/作业**: A parallel computation consisting of multiple tasks that gets spawned in response to a Spark action (e.g. save, `collect`); you'll see this term used in the driver's logs.
- **Stage/阶段**: Each job gets divided into smaller sets of tasks called stages that depend on each other (similar to the map and reduce stages in MapReduce); you'll see this term used in the driver's logs.

## Spark Standalone Mode

## Submitting Applications

The `spark-submit` script in Spark’s bin directory is used to launch applications on a cluster. 

If your code depends on other projects, you will need to package them alongside your application in order to distribute the code to a Spark cluster. To do this, create an assembly jar (or “uber” jar) containing your code and its dependencies.

- `--py-files`
For Python, you can use the `--py-files` argument of `spark-submit` to add `.py`, `.zip` or `.egg` files to be distributed with your application. If you depend on multiple Python files we recommend packaging them into a `.zip` or `.egg`. For third-party Python dependencies, see [Python Package Management](https://spark.apache.org/docs/latest/api/python/tutorial/python_packaging.html).

```shell
./bin/spark-submit \
  --class <main-class> \
  --master <master-url> \
  --deploy-mode <deploy-mode> \
  --conf <key>=<value> \
  ... # other options
  <application-jar> \
  [application-arguments]
```

- `--properties-file`
The `spark-submit` script can load default Spark configuration values from a properties file and pass them on to your application. The file can be specified via the `--properties-file` parameter. When this is not specified, by default Spark will read options from `conf/spark-defaults.conf` in the `SPARK_HOME` directory.

- `--jars`
When using spark-submit, the application jar along with any jars included with the `--jars` option will be automatically transferred to the cluster. URLs supplied after `--jars` must be separated by commas. That list is included in the driver and executor classpaths.

- `--packages`, `--repositories`
Users may also include any other dependencies by supplying a comma-delimited list of Maven coordinates with `--packages`. All transitive dependencies will be handled when using this command. Additional repositories (or resolvers in SBT) can be added in a comma-delimited fashion with the flag `--repositories`.

## Monitoring

## Job Scheduling

## Security

## Hardware Provisioning

# Configuration
* https://spark.apache.org/docs/latest/configuration.html

# Spark Connect
* https://spark.apache.org/spark-connect/

clients: Java/Scala, Python, Go, Swift, .NET

# See Also
* [third party projects](https://spark.apache.org/third-party-projects.html): This page tracks external software projects that supplement Apache Spark and add to its ecosystem.
* [Deep Dive into Spark SQL's Catalyst Optimizer](https://www.databricks.com/blog/2015/04/13/deep-dive-into-spark-sqls-catalyst-optimizer.html): https://github.com/apache/spark/blob/master/sql/catalyst/pom.xml
* [GraphFrames](https://graphframes.io/): Distributed graph processing on top of Apache Spark. -  GraphX is deprecated in the upstream Apache Spark and is not maintained anymore. GraphFrames project comes with its own fork of GraphX: `org.apache.spark.graphframes.graphx`.

## RTFSC

```shell
 $ grep "<module>" pom.xml
    <module>common/sketch</module>             Spark Project Sketch
    <module>common/kvstore</module>            Spark Project Local DB
    <module>common/network-common</module>     Spark Project Networking
    <module>common/network-shuffle</module>    Spark Project Shuffle Streaming Servic
    <module>common/unsafe</module>             Spark Project Unsafe
    <module>common/tags</module>               Spark Project Tags
    <module>core</module>                      Spark Project Core
    <module>graphx</module>
    <module>mllib</module>
    <module>mllib-local</module>
    <module>tools</module>                     Spark Project Tools
    <module>streaming</module>                 Spark Project Streaming
    <module>sql/catalyst</module>              Spark Project Catalyst
    <module>sql/core</module>                  Spark Project SQL
    <module>sql/hive</module>                  Spark Project Hive
    <module>assembly</module>                  Spark Project Assembly
    <module>examples</module>                  Spark Project Examples
    <module>repl</module>                      Spark Project REPL
    <module>launcher</module>                  Spark Project Launcher
    <module>external/kafka-0-10-token-provider</module>  Kafka 0.10+ Token Provider for Streaming
    <module>external/kafka-0-10</module>                 Spark Integration for Kafka 0.10
    <module>external/kafka-0-10-assembly</module>        Spark Integration for Kafka 0.10 Assembly
    <module>external/kafka-0-10-sql</module>             Kafka 0.10+ Source for Structured Streaming
    <module>external/avro</module>                       Spark Avro

    <!-- modules enables by profiles -->
        <module>external/spark-ganglia-lgpl</module>
        <module>external/kinesis-asl</module>
        <module>external/kinesis-asl-assembly</module>
        <module>external/docker-integration-tests</module>
        <module>resource-managers/yarn</module>
        <module>common/network-yarn</module>
        <module>resource-managers/mesos</module>
        <module>resource-managers/kubernetes/core</module>
        <module>resource-managers/kubernetes/integration-tests</module>
        <module>sql/hive-thriftserver</module>
        <module>hadoop-cloud</module>
```
