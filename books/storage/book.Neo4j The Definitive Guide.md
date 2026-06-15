# Neo4j: The Definitive Guide


# 1. How to Get Value from Graphs in Just Five Days
## Dissonance at ElectricHarmony
## Why Graph Databases?
- Graph Use Cases
- Ultimate Beneficial Ownership Networks
- Real-Time Recommendations
- Law Enforcement
- Cybercrime Networks
## Neo4j
- Native Graph Databases
## Cypher
## The Song Recommendation System: A Proof of Concept
- Day 1
- Day 2
- Day 3
- Day 4
- Day 5

# 2. Importing (Much) More Data
## Database Transactions
- The Beat Heap Box
- Try It: Importing Data from Client Applications
- Parallel Writes
- Offline Import
- Exploring Other Ingestion Tools

# 3. Revisiting Modeling Decisions/重新审视建模决策
## It Depends
## Principles of Modeling
## Properties Versus Nodes
- Properties That Decorate the Result
- Key Takeaways
## Traversing Across Commonalities
- Key Takeaways
## Modeling Concepts as Labels
- Key Takeaways
## Node Fanout
- Key Takeaways
## Supernodes
- Key Takeaways
## Relationship Granularity
- Key Takeaways
## Qualified Relationships
- Bucketed Relationships
- Key Takeaways
## Bidirectional Relationships
- Key Takeaways

# 4. Modeling and Refactoring Patterns/建模和重构模式
## Hyperedges: N-way Relationships
- Key Takeaways
## Time-Based Versioning
- Key Takeaways
## Representing Sequences
- Key Takeaways
- Refactoring Patterns
- Refactoring to Change the Type of a Relationship
- Refactoring to Create a Node from a Property
- Refactoring to Create a Node from a Relationship
- Key Takeaways

# 5. Query Analysis and Tuning/查询分析与优化
## Query Execution
- Pattern Anchors
- Query Profiling
- Row Cardinality
## Matching Disconnected Patterns
## Increasing Anchor Selectivity
- Eliminating Redundant Filter Operations
- Improving Anchor Selectivity in Queries with Predicates
- Indexing Guidelines
## Accessing Properties
## Node Degrees
## Don’t Be Eager!
## Sorting
## I Want to Break Free (of the Planner)
## Cypher Runtimes
## Parameterizing Queries
## Monitoring and Measuring Query Times

# 6. Securing Your Database/保护您的数据库
## Spoofing
- Authentication
- Securing Access via the Neo4j browser
- Best Practices
## Tampering
- Securing Communication Channels
- Securing Data at Rest
- Using Consistency Checks
- Defending Against Cypher Injection Attacks
- Implementing Role-Based Access Control
- Using the Load CSV Command
- Audit Logs
- Constraints
- Backups
## Repudiation
## Information Disclosure
- Query Logs
- Fine-Grained Access Control
- Property Encryption
## Denial of Service
## Elevation of Privilege
- Immutable Privileges
- Least Privileges
- Extensions
- User and Privilege Reviews
- File Permissions
- Patches

# 7. Search/搜索
## What Is Search?
- Text
- Indexes
## Searching for Data
- Partial Searches
## TEXT Indexes
## Full-Text Indexes
- Multitoken Searches
- Phrase Searches
- Wildcard Searches
- Fuzzy Search
## Additional Index and Query Considerations
- Tokenization
- Special Characters: Hashtags and Mentions
- Identifiers, IP Addresses, and Other Nonword Terms
- Stopwords: To Be or Not to Be
- Performance with Graph Patterns

# 8. Advanced Graph Patterns/高级图模式
## Subqueries
- CALL Subqueries
- Post-Union Processing
- Concurrent Transactions with CALL
## Fine-Grained Relationship Types
## Modeling Resolved Entities
- Entity Groups
- Fused Entities
## Quantified Path Patterns: An Entity-Resolution Use Case
## Security Modeling: Labels Versus Properties

# 9. Backup and Restore/备份和恢复
## The Write Path
- Checkpoints
- Transaction-Log Retention
- How Aggressive Is Aggressive?
- A Guided Example
## Backups
- Types of Backups
## Restoring Backups
## Cloud Backups
## Remote Backups and VM Separation
## Designing a Backup Strategy

# 10. Clustering and Sharding/集群和分片
## Clustering for High Availability
- Raft Protocol
- Fault Tolerance
- Secondaries
## Deploying a Cluster
- Cluster Degradation
- Multidatabase Clusters
- How Network Latency Affects Clustering
## Scaling Reads with Secondaries
## Using Secondary Servers for Backups
## Causal Consistency
## The Mythical 1+1 Cluster
## Sharding and Federation

# 11. Observability/可观测性
## Harnessing the Power of Logs
- Types of Logs in Neo4j
- Configuring Neo4j Logs
- Inspecting Logs
- Taming the Query Log
## Unveiling the Power of Metrics
- Enabling Metrics
- Server Load Metrics
- Neo4j Load Metrics
- Neo4j Workload Metrics
## Bringing It All Together: Logs and Metrics with Grafana, Loki, and Prometheus
- Setting Up the Observability Stack
- Visualizing Metrics
- Querying Logs
- Other Tools

# 12. Practical Graph Data Science/实用图数据科学
## Introduction to the Graph Data Science Library
- Algorithms
- The Graph Catalog
## AI-Driven Playlist Communities
- Building a Co-Occurrence Graph
- Using GDS
## Real-World Applications of Community Detection
- Playlist recommendations
- User segmentation
- Influencer discovery
- Behavioral clusters
- Content licensing strategy

# 13. The Future of Graphs with Generative AI/生成式AI在图领域的未来
## Knowledge Graphs
- Applications of Knowledge Graphs
- Customer 360
- Cybersecurity
- Life Sciences
- Retail
- Criminal Investigations
## GraphRAG
- What About Vector Search?
## Agentic AI Architectures
## Knowledge Graph Creation
## A Practical Example: Playlist Recommendations from Natural Language
- Step 1: Communities from GDS
- Step 2: Generate Summaries and Questions with an LLM
- Step 3: Vectorize and Store in Neo4j
- Step 4: The User Asks a Question
- Step 5: Generate an answer
- Step 6: Wrapping Up

# See Also
