# Streaming Systems: The What, Where, When, and How of Large-Scale Data Processing

- I. The Beam Model: 1-5
- II. Streams and Tables: 6-10

# 1. Streaming 101
## Terminology: What Is Streaming?/什么是流?
- On the Greatly Exaggerated Limitations of Streaming
- Event Time Versus Processing Time
## Data Processing Patterns/数据处理模式
- Bounded Data
- Unbounded Data: Batch
- Unbounded Data: Streaming

# 2. The What, Where, When, and How of Data Processing/数据处理的什么/哪里/何时/如何
## Roadmap
## Batch Foundations: What and Where
- What: Transformations
- Where: Windowing
## Going Streaming: When and How
- When: The Wonderful Thing About Triggers Is Triggers Are Wonderful Things!
- When: Watermarks
- When: Early/On-Time/Late Triggers FTW!
- When: Allowed Lateness (i.e., Garbage Collection)
- How: Accumulation

# 3. Watermarks/水位线
## Definition
## Source Watermark Creation
- Perfect Watermark Creation
- Heuristic Watermark Creation
## Watermark Propagation
- Understanding Watermark Propagation
- Watermark Propagation and Output Timestamps
- The Tricky Case of Overlapping Windows
## Percentile Watermarks
## Processing-Time Watermarks
## Case Studies
- Case Study: Watermarks in Google Cloud Dataflow
- Case Study: Watermarks in Apache Flink
- Case Study: Source Watermarks for Google Cloud Pub/Sub

# 4. Advanced Windowing/高级窗口
## When/Where: Processing-Time Windows
- Event-Time Windowing
- Processing-Time Windowing via Triggers
- Processing-Time Windowing via Ingress Time
## Where: Session Windows
## Where: Custom Windowing
- Variations on Fixed Windows
- Variations on Session Windows
- One Size Does Not Fit All

# 5. Exactly-Once and Side Effects/精确一次和副作用
## Why Exactly Once Matters
## Accuracy Versus Completeness
- Side Effects
- Problem Definition
## Ensuring Exactly Once in Shuffle
## Addressing Determinism
## Performance
- Graph Optimization
- Bloom Filters
- Garbage Collection
## Exactly Once in Sources
## Exactly Once in Sinks
## Use Cases
- Example Source: Cloud Pub/Sub
- Example Sink: Files
- Example Sink: Google BigQuery
## Other Systems
- Apache Spark Streaming
- Apache Flink

# 6. Streams and Tables/流与表
## Stream-and-Table Basics Or: a Special Theory of Stream and Table Relativity
- Toward a General Theory of Stream and Table Relativity
## Batch Processing Versus Streams and Tables
- A Streams and Tables Analysis of MapReduce
- Reconciling with Batch Processing
## What, Where, When, and How in a Streams and Tables World
- What: Transformations
- Where: Windowing
- When: Triggers
- How: Accumulation
- A Holistic View of Streams and Tables in the Beam Model
## A General Theory of Stream and Table Relativity

# 7. The Practicalities of Persistent State/持久状态的实用性
## Motivation
- The Inevitability of Failure
- Correctness and Efficiency
## Implicit State
- Raw Grouping
- Incremental Combining
## Generalized State
- Case Study: Conversion Attribution
- Conversion Attribution with Apache Beam

# 8. Streaming SQL/流式SQL
## What Is Streaming SQL?
- Relational Algebra
- Time-Varying Relations
- Streams and Tables
## Looking Backward: Stream and Table Biases
- The Beam Model: A Stream-Biased Approach
- The SQL Model: A Table-Biased Approach
## Looking Forward: Toward Robust Streaming SQL
- Stream and Table Selection
- Temporal Operators

# 9. Streaming Joins/流式联接
## All Your Joins Are Belong to Streaming
## Unwindowed Joins
- FULL OUTER
- LEFT OUTER
- RIGHT OUTER
- INNER
- ANTI
- SEMI
## Windowed Joins
- Fixed Windows
- Temporal Validity

# 10. The Evolution of Large-Scale Data Processing/大规模数据处理的演变
- MapReduce
- Hadoop
- Flume
- Storm
- Spark
- MillWheel
- Kafka
- Cloud Dataflow
- Flink
- Beam

