# Neo4j: The Definitive Guide - Hands-On Recipes for Production-Ready Graph Implementations

Docker image: `neo4j:5.26-enterprise`

# 1. How to Get Value from Graphs in Just Five Days
- Dissonance at ElectricHarmony
- Why Graph Databases?
  - Graph Use Cases
  - Ultimate Beneficial Ownership Networks
  - Real-Time Recommendations
  - Law Enforcement
  - Cybercrime Networks
- Neo4j
  - Native Graph Databases
- Cypher
- The Song Recommendation System: A Proof of Concept/音乐推荐系统
  - dataset: https://github.com/neo4j-the-definitive-guide/book/tree/main/docker/import

goal
- Detect similar playlists/播放列表 based on how many tracks/曲目 they share.
- Compute a track recommendation based on similar playlists; find similar playlists that share the same last track. The more similar the playlist, the better the recommendation.
- Discard recommendations for tracks that are too popular.
- Compute recommendations in less than 200ms.

# 2. Importing (Much) More Data
- Database Transactions
  - The Beat Heap Box
  - Try It: Importing Data from Client Applications
    - Python driver: `pip install neo4j` - https://neo4j.com/docs/python-manual/current/
  - Parallel Writes
  - Offline Import
  - Exploring Other Ingestion Tools

# 3. Revisiting Modeling Decisions/重新审视建模决策
- It Depends
- Principles of Modeling/建模原则
  - human intuitiveness
  - query simplicity
  - query performance
  - read/write trade-off
- Properties Versus Nodes/属性与节点
  - Properties That Decorate the Result/修饰结果的属性
- Traversing Across Commonalities/遍历共性
- Modeling Concepts as Labels/将概念建模为标签
- Node Fanout/节点扇出
- Supernodes/超级节点
- Relationship Granularity/关系粒度
- Qualified Relationships/限定关系
  - Bucketed Relationships/分桶关系
- Bidirectional Relationships/双向关系

Figure 3-2. Model A models genres as a property of the artist.

Figure 3-3. Genres are nodes and the album is a property in Model B.

Figure 3-4. Model C with a TRACK_VERSION node and genres as labels

# 4. Modeling and Refactoring Patterns/建模和重构模式
- Hyperedges: N-way Relationships/超边: N路关系
  - 超边节点
- Time-Based Versioning/基于时间的版本化
- Representing Sequences/表示序列
  - Refactoring Patterns
  - Refactoring to Change the Type of a Relationship
  - Refactoring to Create a Node from a Property
  - Refactoring to Create a Node from a Relationship

# 5. Query Analysis and Tuning/查询分析与优化
- Query Execution/查询执行
  - Pattern Anchors/模式锚点
    - global graph query
    - querying based on labels
    - a more selective query
  - Query Profiling/查询分析
    - `db.stats`
    - `EXPLAIN`
    - `PROFILE`
  - Row Cardinality/行基数
- Matching Disconnected Patterns/匹配不连通模式
- Increasing Anchor Selectivity/增加锚点选择性
  - Eliminating Redundant Filter Operations
  - Improving Anchor Selectivity in Queries with Predicates
    - indexes: token lookup index/range index, text index, point index
  - Indexing Guidelines
- Accessing Properties/访问属性
  - defer reading properties of nodes and relationships to as late as possible
- Node Degrees/节点的度
  - `getDegree`
- Don’t Be Eager!
  - implicit eagerness
  - explicit eagerness: aggregate functions, sort operations not using an index, `DISTINCT`
- Sorting/排序
- I Want to Break Free (of the Planner)
  - `USING INDEX`
- Cypher Runtimes/Cypher运行时
  - pipelined: default for Enterprise edition
  - parallel
  - slotted: default for Community edition
  - `CYPHER runtime = ...`
- Parameterizing Queries/参数化查询
  - query caches
  - `$parameter`
- Monitoring and Measuring Query Times/监控和度量查询时间
  - load testing: JMeter
  - query log: `db.logs.query.enabled`, `db.logs.query.threshold`

# 6. Securing Your Database/保护数据库
- Spoofing
  - Authentication
  - Securing Access via the Neo4j browser
  - Best Practices
- Tampering
  - Securing Communication Channels
  - Securing Data at Rest
  - Using Consistency Checks
  - Defending Against Cypher Injection Attacks
  - Implementing Role-Based Access Control
  - Using the Load CSV Command
  - Audit Logs
  - Constraints
  - Backups
- Repudiation
- Information Disclosure
  - Query Logs
  - Fine-Grained Access Control
  - Property Encryption
- Denial of Service
- Elevation of Privilege
  - Immutable Privileges
  - Least Privileges
  - Extensions
  - User and Privilege Reviews
  - File Permissions
  - Patches

# 7. Search/搜索
- What Is Search?
  - Text
  - Indexes
    - text index: queries using operator `STARTS WITH`, `ENDS WITH`, `CONTAINS`
    - full-text index: proximity search, relevance ranking
    - vector index: embedding, vector similarity functions
- Searching for Data
  - Partial Searches
- TEXT Indexes
- Full-Text Indexes
  - `CALL db.index.fulltext.queryNodes(...)`
  - Multitoken Searches
  - Phrase Searches
  - Wildcard Searches
  - Fuzzy Search
- Additional Index and Query Considerations
  - Tokenization
  - Special Characters: Hashtags and Mentions
  - Identifiers, IP Addresses, and Other Nonword Terms
  - Stopwords: To Be or Not to Be
  - Performance with Graph Patterns

# 8. Advanced Graph Patterns/高级图模式
- Subqueries/子查询
  - `CALL` Subqueries
  - Post-Union Processing
  - Concurrent Transactions with `CALL`
- Fine-Grained Relationship Types/细粒度关系模型
- Modeling Resolved Entities/建模已解析的实体
  - Entity Groups/实体分组
  - Fused Entities/融合实体
    - v5.26: dynamic labels and relationship types
- Quantified Path Patterns(QPP): An Entity-Resolution Use Case/量化路径模式
  - ex: `[...]{0,11}`
- Security Modeling: Labels Versus Properties/安全建模
  - LBAC(label-based access control)
  - PBAC(property-based access control)

# 9. Backup and Restore/备份和恢复
- The Write Path
  - Checkpoints
  - Transaction-Log Retention
  - How Aggressive Is Aggressive?
  - A Guided Example
- Backups
  - Types of Backups
- Restoring Backups
- Cloud Backups
- Remote Backups and VM Separation
- Designing a Backup Strategy

# 10. Clustering and Sharding/集群和分片
- Clustering for High Availability
  - Raft Protocol
  - Fault Tolerance
  - Secondaries
- Deploying a Cluster
  - Cluster Degradation
  - Multidatabase Clusters
  - How Network Latency Affects Clustering
- Scaling Reads with Secondaries
- Using Secondary Servers for Backups
- Causal Consistency
- The Mythical 1+1 Cluster
- Sharding and Federation

# 11. Observability/可观测性
- Harnessing the Power of Logs
  - Types of Logs in Neo4j
    - N4o4j, Debug, Query, Security
  - Configuring Neo4j Logs
    - user-logs.xml, server-logs.xml
  - Inspecting Logs
  - Taming the Query Log
- Unveiling the Power of Metrics
  - Enabling Metrics
  - Server Load Metrics
  - Neo4j Load Metrics
  - Neo4j Workload Metrics
- Bringing It All Together: Logs and Metrics with Grafana, Loki, and Prometheus
  - Setting Up the Observability Stack
    - Grafana: interactive visual interface
    - Prometheus: metrics collector
    - Loki: logs aggregator service
    - Node exporter: for collection OS metrics
    - Promtail: logs collector
  - Visualizing Metrics
  - Querying Logs
  - Other Tools
    - Neo4j Ops Manager
    - Neo4j Aura: Query analyzer

# 12. Practical Graph Data Science/实用图数据科学
- Introduction to the Graph Data Science(GDS) Library
  - Algorithms
  - The Graph Catalog
    - in memory, graph projections/内存中图投影
- AI-Driven Playlist Communities
  - Building a Co-Occurrence Graph
  - Using GDS
    - `CALL gds.graph.project`
    - `CALL gds.louvain.[execution-mode]`
      - execution mode: `stream`, `write`, `stats`, `mutate`
- Real-World Applications of Community Detection/社区检测的实际应用
  - Playlist recommendations/歌单推荐
  - User segmentation/用户细分
  - Influencer discovery/影响者发现
  - Behavioral clusters/行为聚类
  - Content licensing strategy/内容授权策略

# 13. The Future of Graphs with Generative AI/生成式AI在图领域的未来
- Knowledge Graphs/知识图谱
  - Applications of Knowledge Graphs
    - Customer 360/客户360度视图
    - Cybersecurity/网络安全
    - Life Sciences/生命科学
    - Retail/零售业
    - Criminal Investigations/刑事调查
- GraphRAG
  - What About Vector Search?
- Agentic AI Architectures/智能体AI架构
  - Neo4j MCP Servers: `mcp-neo4j-cypher`, `mcp-neo4j-memory`, `mcp-neo4j-cloud-aura-api`
- Knowledge Graph Creation/知识图谱创建
  - https://llm-graph-builder.neo4jlabs.com/readonly
    - transforms documents, PDFs, video transcripts, and the like into a lexical graph of documents and chunks, which are stored, with their embeddings, in your Neo4j database, in addition to an entity graph that stores nodes and their relationships to the document chunks
    - LangChain, LlamaIndex, the GraphRAG Python package
- A Practical Example: Playlist Recommendations from Natural Language/基于自然语言的歌单推荐
  - Step 1: Communities from GDS
  - Step 2: Generate Summaries and Questions with an LLM
  - Step 3: Vectorize and Store in Neo4j
  - Step 4: The User Asks a Question
  - Step 5: Generate an answer
  - Step 6: Wrapping Up

# See Also
* openCypher: https://opencypher.org/
* GQL(Graph Query Language): https://gqlstandards.org/
* Text2Cypher: Bridging Natural Language and Graph Databases: https://arxiv.org/pdf/2412.10064