# Streaming Databases: Unifying Batch and Stream Processing/流式数据库: 统一批流处理

# 1. Streaming Foundations/流基础
## Turning the Database Inside Out
## Externalizing Database Features
- Write-Ahead Log
- Streaming Platforms
- Materialized Views
## Use Case: Clickstream Analysis
- Understanding Transactions and Events
- Domain-Driven Design
## Context Enrichment
## Change Data Capture
## Connectors
- Connector Middleware
- Embedded
- Custom-Built

# 2. Stream Processing Platforms/流处理平台
## Stateful Transformations
## Data Pipelines
- ELT Limitations
- Stream Processing with ELT
## Stream Processors
- Popular Stream Processors
- Newer Stream Processors
## Emulating Materialized Views in Apache Spark
## Two Types of Streams
- Append Stream
- Debezium Change Data
- Materialized Views

# 3. Serving Real-Time Data/提供实时数据
## Real-Time Expectations
## Choosing an Analytical Data Store
## Sourcing from a Topic
## Ingestion Transformations
## OLTP Versus OLAP
- ACID
- Row- Versus Column-Based Optimization
## Queries Per Second and Concurrency
## Indexing
## Serving Analytical Results
- Synchronous Queries
- Asynchronous Queries
- Push Versus Pull Queries

# 4. Materialized Views/物化视图
## Views, Materialized Views, and Incremental Updates
## Change Data Capture
## Push Versus Pull Queries
## CDC and Upsert
## Joining Streams
- Apache Calcite
- Clickstream Use Case

# 5. Introduction to Streaming Databases/流式数据库介绍
## Identifying the Streaming Database
- Column-Based Streaming Database
- Row-Based Streaming Database
- Edge Streaming-Like Databases
## SQL Expressivity
## Streaming Debuggability
- Advantages of Debugging in Streaming Databases
- SQL Is Not a Silver Bullet
## Streaming Database Implementations
## Streaming Database Architecture
## ELT with Streaming Databases

# 6. Consistency/一致性
## A Toy Example
- Transactions
- Analyzing the Transactions
## Comparing Consistency Across Stream Processing Systems
- Flink SQL
- ksqlDB
- Proton (Timeplus)
- RisingWave
- Materialize
- Pathway
- Out-of-Order Messages
## Going Beyond Eventual Consistency
- Why Do Eventually Consistent Stream Processors Fail 
- the Toy Example?
- How Do Internally Consistent Stream Processing Systems 
- Pass the Toy Example?
- How Can We Fix Eventually Consistent Stream Processing 
- Systems to Pass the Toy Example?
## Consistency Versus Latency

# 7. Emergence of Other Hybrid Data Systems/其他混合数据系统的出现
## Data Planes
## Hybrid Transactional/Analytical Database
## Other Hybrid Databases
## Motivations for Hybrid Systems
## The Influence of PostgreSQL on Hybrid Databases
## Near-Edge Analytics
## Next-Generation Hybrid Databases
- Next-Generation Streaming OLTP Databases
- Next-Generation Streaming RTOLAP Databases
- Next-Generation HTAP Databases

# 8. Zero-ETL or Near-Zero-ETL/零ETL或近零ETL
## ETL Model
## Zero-ETL
## Near-Zero-ETL
- PeerDB
- Proton
- Embedded OLAP
- Data Gravity and Replication
- Analytical Data Reduction
## Lambda Architecture
- Apache Pinot Hybrid Tables
- Pipeline Configurations

# 9. The Streaming Plane/流平面
## Data Gravity
## Components of the Streaming Plane
## Streaming Plane Infrastructure
## Operational Analytics
## Data Mesh
- Pillars of a Data Mesh
- Challenge of a Data Mesh
## Streaming Data Mesh with Streaming Plane and Streaming Databases
- Data Locality
- Data Replication

# 10. Deployment Models/部署模型
## Consistent Streaming Database
## Consistent Streaming Processor and RTOLAP
## Eventually Consistent OLAP Streaming Database
## Eventually Consistent Stream Processor and RTOLAP
## Eventually Consistent Stream Processor and HTAP
## ksqlDB
## Incremental View Maintenance
## Postgres Multicorn Foreign Data Wrapper
## When to Use Code-Based Stream Processors
## When to Use Lakehouse/Streamhouse Technologies
## Caching Technologies
## Where to Do Processing and Querying in General?
- The Four “Where” Questions
- An Analytical Use Case
- Consequences

# 11. Future State of Real-Time Data/实时数据的未来
## The Convergence of the Data Planes
## Graph Databases
- Memgraph
- thatDot/Quine
## Vector Databases
- Milvus 2.x: Streaming as the Central Backbone
- RTOLAP Databases: Adding Vector Search
## Incremental View Maintenance
- pg_ivm
- Hydra
- Epsio
- Feldera
- PeerDB
## Data Wrapping and Postgres Multicorn
## Classical Databases
## Data Warehouses
- BigQuery
- Redshift
- Snowflake
## Lakehouse
- Delta Lake
- Apache Paimon
- Apache Iceberg
- Apache Hudi
- OneTable or XTable
- The Relationship of Streaming and Lakehouses
## Conclusion

