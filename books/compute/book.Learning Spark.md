# Learning Spark: Lightning-Fast Data Analytics

Version: 3.0.

# 1. Introduction to Apache Spark: A Unified Analytics Engine
## The Genesis of Spark
- Big Data and Distributed Computing at Google
- Hadoop at Yahoo!
- Spark’s Early Years at AMPLab
## What Is Apache Spark?
- Speed
- Ease of Use
- Modularity
- Extensibility
## Unified Analytics/统一分析
- Apache Spark Components as a Unified Stack
- Apache Spark’s Distributed Execution
## The Developer’s Experience
- Who Uses Spark, and for What?
- Community Adoption and Expansion

# 2. Downloading Apache Spark and Getting Started
## Step 1: Downloading Apache Spark
- Spark’s Directories and Files
## Step 2: Using the Scala or PySpark Shell
- Using the Local Machine
## Step 3: Understanding Spark Application Concepts
- Spark Application and SparkSession
- Spark Jobs
- Spark Stages
- Spark Tasks
## Transformations, Actions, and Lazy Evaluation/转换, 动作和惰性求值
- Narrow and Wide Transformations
## The Spark UI
## Your First Standalone Application
- Counting M&Ms for the Cookie Monster
- Building Standalone Applications in Scala

# 3. Apache Spark’s Structured APIs
## Spark: What’s Underneath an RDD?
## Structuring Spark
- Key Merits and Benefits
## The DataFrame API
- Spark’s Basic Data Types
- Spark’s Structured and Complex Data Types
- Schemas and Creating DataFrames
- Columns and Expressions
- Rows
- Common DataFrame Operations
- End-to-End DataFrame Example
## The Dataset API
- Typed Objects, Untyped Objects, and Generic Rows
- Creating Datasets
- Dataset Operations
- End-to-End Dataset Example
## DataFrames Versus Datasets
- When to Use RDDs
## Spark SQL and the Underlying Engine
- The Catalyst Optimizer

# 4. Spark SQL and DataFrames: Introduction to Built-in Data Sources
## Using Spark SQL in Spark Applications
- Basic Query Examples
## SQL Tables and Views
- Managed Versus UnmanagedTables
- Creating SQL Databases and Tables
- Creating Views
- Viewing the Metadata
- Caching SQL Tables
- Reading Tables into DataFrames
## Data Sources for DataFrames and SQL Tables
- DataFrameReader
- DataFrameWriter
- Parquet
- JSON
- CSV
- Avro
- ORC
- Images
- Binary Files

# 5. Spark SQL and DataFrames: Interacting with External Data Sources
## Spark SQL and Apache Hive
- User-Defined Functions
## Querying with the Spark SQL Shell, Beeline, and Tableau
- Using the Spark SQL Shell
- Working with Beeline
- Working with Tableau
## External Data Sources
- JDBC and SQL Databases
- PostgreSQL
- MySQL
- Azure Cosmos DB
- MS SQL Server
- Other External Sources
## Higher-Order Functions in DataFrames and Spark SQL
- Option 1: Explode and Collect
- Option 2: User-Defined Function
- Built-in Functions for Complex Data Types
- Higher-Order Functions
## Common DataFrames and Spark SQL Operations
- Unions
- Joins
- Windowing
- Modifications

# 6. Spark SQL and Datasets
## Single API for Java and Scala
- Scala Case Classes and JavaBeans for Datasets
## Working with Datasets
- Creating Sample Data
- Transforming Sample Data
## Memory Management for Datasets and DataFrames
## Dataset Encoders
- Spark’s Internal Format Versus Java Object Format
- Serialization and Deserialization (SerDe)
## Costs of Using Datasets
- Strategies to Mitigate Costs

# 7. Optimizing and Tuning Spark Applications
## Optimizing and Tuning Spark for Efficiency
- Viewing and Setting Apache Spark Configurations
- Scaling Spark for Large Workloads
## Caching and Persistence of Data
- DataFrame.cache()
- DataFrame.persist()
- When to Cache and Persist
- When Not to Cache and Persist
## A Family of Spark Joins
- Broadcast Hash Join
- Shuffle Sort Merge Join
## Inspecting the Spark UI
- Journey Through the Spark UI Tabs

# 8. Structured Streaming
## Evolution of the Apache Spark Stream Processing Engine
- The Advent of Micro-Batch Stream Processing
- Lessons Learned from Spark Streaming (DStreams)
- The Philosophy of Structured Streaming
## The Programming Model of Structured Streaming
## The Fundamentals of a Structured Streaming Query
- Five Steps to Define a Streaming Query
- Under the Hood of an Active Streaming Query
- Recovering from Failures with Exactly-Once Guarantees
- Monitoring an Active Query
## Streaming Data Sources and Sinks
- Files
- Apache Kafka
- Custom Streaming Sources and Sinks
## Data Transformations
- Incremental Execution and Streaming State
- Stateless Transformations
- Stateful Transformations
## Stateful Streaming Aggregations
- Aggregations Not Based on Time
- Aggregations with Event-Time Windows
## Streaming Joins
- Stream–Static Joins
- Stream–Stream Joins
## Arbitrary Stateful Computations
- Modeling Arbitrary Stateful Operations with mapGroupsWithState()
- Using Timeouts to Manage Inactive Groups
- Generalization with flatMapGroupsWithState()
## Performance Tuning

# 9. Building Reliable Data Lakes with Apache Spark
## The Importance of an Optimal Storage Solution
## Databases/数据库
- A Brief Introduction to Databases
- Reading from and Writing to Databases Using Apache Spark
- Limitations of Databases
## Data Lakes/数据湖
- A Brief Introduction to Data Lakes
- Reading from and Writing to Data Lakes using Apache Spark
- Limitations of Data Lakes
## Lakehouses/湖仓: The Next Step in the Evolution of Storage Solutions
- Apache Hudi
- Apache Iceberg
- Delta Lake
## Building Lakehouses with Apache Spark and Delta Lake
- Configuring Apache Spark with Delta Lake
- Loading Data into a Delta Lake Table
- Loading Data Streams into a Delta Lake Table
- Enforcing Schema on Write to Prevent Data Corruption
- Evolving Schemas to Accommodate Changing Data
- Transforming Existing Data
- Auditing Data Changes with Operation History
- Querying Previous Snapshots of a Table with Time Travel

# 10. Machine Learning with MLlib
## What Is Machine Learning?
- Supervised Learning
- Unsupervised Learning
- Why Spark for Machine Learning?
## Designing Machine Learning Pipelines
- Data Ingestion and Exploration
- Creating Training and Test Data Sets
- Preparing Features with Transformers
- Understanding Linear Regression
- Using Estimators to Build Models
- Creating a Pipeline
- Evaluating Models
- Saving and Loading Models
## Hyperparameter Tuning
- Tree-Based Models
- k-Fold Cross-Validation
- Optimizing Pipelines

# 11. Managing, Deploying, and Scaling Machine Learning Pipelines with Apache Spark
## Model Management
- MLflow
## Model Deployment Options with MLlib
- Batch
- Streaming
- Model Export Patterns for Real-Time Inference
## Leveraging Spark for Non-MLlib Models
- Pandas UDFs
- Spark for Distributed Hyperparameter Tuning

# 12. Epilogue: Apache Spark 3.0
## Spark Core and Spark SQL
- Dynamic Partition Pruning
- Adaptive Query Execution
- SQL Join Hints
- Catalog Plugin API and DataSourceV2
- Accelerator-Aware Scheduler
## Structured Streaming
## PySpark, Pandas UDFs, and Pandas Function APIs
- Redesigned Pandas UDFs with Python Type Hints
- Iterator Support in Pandas UDFs
- New Pandas Function APIs
## Changed Functionality
- Languages Supported and Deprecated
- Changes to the DataFrame and Dataset APIs
- DataFrame and SQL Explain Commands

# See Also
* Mastering Spark with R (O’Reilly) by Javier Luraschi, Kevin Kuo, and Edgar Ruiz
* not included features: RDD, GraphX, Catalyst optimizer, catalog, DataSource V2 data sinks and sources
* The Catalyst Optimizer, Project Tungsten
* Lakehouses:
  * Apache Hudi: https://hudi.apache.org/
  * Apache Iceberg: https://iceberg.apache.org/
  * Delta Lake: https://delta.io/
    * Releases: https://docs.delta.io/releases/
* [MLflow](https://github.com/mlflow/mlflow): The open source AI engineering platform for agents, LLMs, and ML models. MLflow enables teams of all sizes to debug, evaluate, monitor, and optimize production-quality AI applications while controlling costs and managing access to models and data.
  * Tracking
  * Projects
  * Models
  * Registry
* Pandas UDF
* Spark for Distributed Hypterparameter Tuning
  * [Joblib](https://github.com/joblib/joblib): Joblib is a set of tools to provide lightweight pipelining in Python. - https://github.com/joblib/joblib-spark
  * [Hyperopt](https://github.com/hyperopt/hyperopt): Distributed Asynchronous Hyperparameter Optimization in Python. - https://hyperopt.github.io/hyperopt/scaleout/spark/