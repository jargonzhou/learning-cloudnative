# Neo4j Graph Data Science

# Graph Data Science (GDS) library
* https://neo4j.com/docs/graph-data-science/current/

## Common usage
## Graph management

A **graph** in GDS is an in-memory structure containing **nodes** connected by **relationships**. Both nodes and relationships can hold numerical attributes/数值属性 (**properties**). Graphs are stored using compressed data structures optimized for topology and property lookup operations.

Each graph has a name that can be used as a reference for management operations, or in analytical workflows that require the same graph to be processed several times. These references are stored in the **graph catalog/图目录**.

## Graph algorithms
## Machine learning
## Production deployment
## Python client

## Appendix - Operations reference
* https://neo4j.com/docs/graph-data-science/current/operations-reference/appendix-a/

### Graph Catalog/图目录

> Production-quality tier

Table 1. List of all production-quality graph operations in the GDS library.

| Description                                                   | Operation                                                |
| ------------------------------------------------------------- | -------------------------------------------------------- |
| Project Graph                                                 | `gds.graph.project` Procedure                            |
|                                                               | `gds.graph.project.estimate` Procedure                   |
|                                                               | `gds.graph.project.cypher` Procedure Deprecated          |
|                                                               | `gds.graph.project.cypher.estimate` Procedure Deprecated |
|                                                               | `gds.graph.project` Function (aggregation function)      |
| Check if a graph exists                                       | `gds.graph.exists` Procedure                             |
|                                                               | `gds.graph.exists` Function                              |
| List graphs                                                   | `gds.graph.list` Procedure                               |
| Drop node properties from a named graph                       | `gds.graph.nodeProperties.drop` Procedure                |
| Delete relationships from a named graph                       | `gds.graph.relationships.drop` Procedure                 |
| Remove a named graph from memory                              | `gds.graph.drop` Procedure                               |
| Stream a single node property to the procedure caller         | `gds.graph.nodeProperty.stream` Procedure                |
| Stream node properties to the procedure caller                | `gds.graph.nodeProperties.stream` Procedure              |
| Stream a single relationship property to the procedure caller | `gds.graph.relationshipProperty.stream` Procedure        |
| Stream relationship properties to the procedure caller        | `gds.graph.relationshipProperties.stream` Procedure      |
|                                                               | `gds.graph.relationshipProperties.write` Procedure       |
| Write node properties to Neo4j                                | `gds.graph.nodeProperties.write` Procedure               |
| Write relationships to Neo4j                                  | `gds.graph.relationship.write` Procedure                 |
| Graph Export                                                  | `gds.graph.export` Procedure                             |

> Beta Tier

Table 2. List of all beta graph operations in the GDS library.

| Description                                            | Operation                                                 |
| ------------------------------------------------------ | --------------------------------------------------------- |
| Project a graph from a graph in the catalog            | `gds.graph.filter` Procedure                              |
| Generate Random Graph                                  | `gds.graph.generate` Procedure                            |
| CSV Export                                             | `gds.graph.export.csv` Procedure                          |
|                                                        | `gds.graph.export.csv.estimate` Procedure                 |
| Stream relationship topologies to the procedure caller | `gds.graph.relationships.stream` Procedure                |
| Convert directed relationships to undirected           | `gds.graph.relationships.toUndirected` Procedure          |
|                                                        | `gds.graph.relationships.toUndirected.estimate` Procedure |
| Collapse Path                                          | `gds.collapsePath.mutate` Procedure                       |

> Alpha Tier

Table 3. List of all alpha graph operations in the GDS library.

| Description                                                | Operation                                   |
| ---------------------------------------------------------- | ------------------------------------------- |
| Drop a graph property from a named graph                   | `gds.graph.graphProperty.drop` Procedure    |
| Stream a graph property to the procedure caller            | `gds.graph.graphProperty.stream` Procedure  |
| Sample a subgraph using random walk with restarts          | `gds.graph.sample.rwr` Procedure            |
| Sample a subgraph using common neighbor aware random walks | `gds.graph.sample.cnarw` Procedure          |
|                                                            | `gds.graph.sample.cnarw.estimate` Procedure |
| Add node labels to the in-memory graph                     | `gds.graph.nodeLabel.mutate` Procedure      |
| Write node labels to the database                          | `gds.graph.nodeLabel.write` Procedure       |



### Graph Algorithms/图算法

> Production-quality tier

Table 1. List of all production-quality algorithms in the GDS library.

| Algorithm name                     | Operation                                                  |
| ---------------------------------- | ---------------------------------------------------------- |
| All Shortest Paths Delta-Stepping  | `gds.allShortestPaths.delta.stream` Procedure              |
|                                    | `gds.allShortestPaths.delta.stream.estimate` Procedure     |
|                                    | `gds.allShortestPaths.delta.write` Procedure               |
|                                    | `gds.allShortestPaths.delta.write.estimate` Procedure      |
|                                    | `gds.allShortestPaths.delta.mutate` Procedure              |
|                                    | `gds.allShortestPaths.delta.mutate.estimate` Procedure     |
|                                    | `gds.allShortestPaths.delta.stats` Procedure               |
|                                    | `gds.allShortestPaths.delta.stats.estimate` Procedure      |
| All Shortest Paths Dijkstra        | `gds.allShortestPaths.dijkstra.stream` Procedure           |
|                                    | `gds.allShortestPaths.dijkstra.stream.estimate` Procedure  |
|                                    | `gds.allShortestPaths.dijkstra.write` Procedure            |
|                                    | `gds.allShortestPaths.dijkstra.write.estimate` Procedure   |
|                                    | `gds.allShortestPaths.dijkstra.mutate` Procedure           |
|                                    | `gds.allShortestPaths.dijkstra.mutate.estimate` Procedure  |
| All Shortest Paths                 | `gds.allShortestPaths.stream` Procedure                    |
|                                    | `gds.allShortestPaths.stream.estimate` Procedure           |
| ArticleRank                        | `gds.articleRank.mutate` Procedure                         |
|                                    | `gds.articleRank.mutate.estimate` Procedure                |
|                                    | `gds.articleRank.write` Procedure                          |
|                                    | `gds.articleRank.write.estimate` Procedure                 |
|                                    | `gds.articleRank.stream` Procedure                         |
|                                    | `gds.articleRank.stream.estimate` Procedure                |
|                                    | `gds.articleRank.stats` Procedure                          |
|                                    | `gds.articleRank.stats.estimate` Procedure                 |
| Articulation Points                | `gds.articulationPoints.mutate` Procedure                  |
|                                    | `gds.articulationPoints.mutate.estimate` Procedure         |
|                                    | `gds.articulationPoints.stats` Procedure                   |
|                                    | `gds.articulationPoints.stats.estimate` Procedure          |
|                                    | `gds.articulationPoints.stream` Procedure                  |
|                                    | `gds.articulationPoints.stream.estimate` Procedure         |
|                                    | `gds.articulationPoints.write` Procedure                   |
|                                    | `gds.articulationPoints.write.estimate` Procedure          |
| Bellman-Ford                       | `gds.bellmanFord.mutate` Procedure                         |
|                                    | `gds.bellmanFord.mutate.estimate` Procedure                |
|                                    | `gds.bellmanFord.stats` Procedure                          |
|                                    | `gds.bellmanFord.stats.estimate` Procedure                 |
|                                    | `gds.bellmanFord.stream` Procedure                         |
|                                    | `gds.bellmanFord.stream.estimate` Procedure                |
|                                    | `gds.bellmanFord.write` Procedure                          |
|                                    | `gds.bellmanFord.write.estimate` Procedure                 |
| Betweenness Centrality             | `gds.betweenness.stream` Procedure                         |
|                                    | `gds.betweenness.stream.estimate` Procedure                |
|                                    | `gds.betweenness.stats` Procedure                          |
|                                    | `gds.betweenness.stats.estimate` Procedure                 |
|                                    | `gds.betweenness.mutate` Procedure                         |
|                                    | `gds.betweenness.mutate.estimate` Procedure                |
|                                    | `gds.betweenness.write` Procedure                          |
|                                    | `gds.betweenness.write.estimate` Procedure                 |
| Breadth First Search               | `gds.bfs.mutate` Procedure                                 |
|                                    | `gds.bfs.mutate.estimate` Procedure                        |
|                                    | `gds.bfs.stream` Procedure                                 |
|                                    | `gds.bfs.stream.estimate` Procedure                        |
|                                    | `gds.bfs.stats` Procedure                                  |
|                                    | `gds.bfs.stats.estimate` Procedure                         |
| Bridges                            | `gds.bridges.stream` Procedure                             |
|                                    | `gds.bridges.stream.estimate` Procedure                    |
| Clique Counting                    | `gds.cliqueCounting.mutate` Procedure                      |
|                                    | `gds.cliqueCounting.mutate.estimate` Procedure             |
|                                    | `gds.cliqueCounting.stats` Procedure                       |
|                                    | `gds.cliqueCounting.stats.estimate` Procedure              |
|                                    | `gds.cliqueCounting.stream` Procedure                      |
|                                    | `gds.cliqueCounting.stream.estimate` Procedure             |
|                                    | `gds.cliqueCounting.write` Procedure                       |
|                                    | `gds.cliqueCounting.write.estimate` Procedure              |
| Closeness Centrality               | `gds.closeness.mutate` Procedure                           |
|                                    | `gds.closeness.mutate.estimate` Procedure                  |
|                                    | `gds.closeness.stats` Procedure                            |
|                                    | `gds.closeness.stats.estimate` Procedure                   |
|                                    | `gds.closeness.stream` Procedure                           |
|                                    | `gds.closeness.stream.estimate` Procedure                  |
|                                    | `gds.closeness.write` Procedure                            |
|                                    | `gds.closeness.write.estimate` Procedure                   |
| Harmonic Centrality                | `gds.closeness.harmonic.mutate` Procedure                  |
|                                    | `gds.closeness.harmonic.mutate.estimate` Procedure         |
|                                    | `gds.closeness.harmonic.stats` Procedure                   |
|                                    | `gds.closeness.harmonic.stats.estimate` Procedure          |
|                                    | `gds.closeness.harmonic.stream` Procedure                  |
|                                    | `gds.closeness.harmonic.stream.estimate` Procedure         |
|                                    | `gds.closeness.harmonic.write` Procedure                   |
|                                    | `gds.closeness.harmonic.write.estimate` Procedure          |
| Conductance                        | `gds.conductance.stream` Procedure                         |
| Topological Sort                   | `gds.dag.topologicalSort.stream` Procedure                 |
| Degree Centrality                  | `gds.degree.mutate` Procedure                              |
|                                    | `gds.degree.mutate.estimate` Procedure                     |
|                                    | `gds.degree.stats` Procedure                               |
|                                    | `gds.degree.stats.estimate` Procedure                      |
|                                    | `gds.degree.stream` Procedure                              |
|                                    | `gds.degree.stream.estimate` Procedure                     |
|                                    | `gds.degree.write` Procedure                               |
|                                    | `gds.degree.write.estimate` Procedure                      |
| Depth First Search                 | `gds.dfs.mutate` Procedure                                 |
|                                    | `gds.dfs.mutate.estimate` Procedure                        |
|                                    | `gds.dfs.stream` Procedure                                 |
|                                    | `gds.dfs.stream.estimate` Procedure                        |
| Eigenvector                        | `gds.eigenvector.mutate` Procedure                         |
|                                    | `gds.eigenvector.mutate.estimate` Procedure                |
|                                    | `gds.eigenvector.write` Procedure                          |
|                                    | `gds.eigenvector.write.estimate` Procedure                 |
|                                    | `gds.eigenvector.stream` Procedure                         |
|                                    | `gds.eigenvector.stream.estimate` Procedure                |
|                                    | `gds.eigenvector.stats` Procedure                          |
|                                    | `gds.eigenvector.stats.estimate` Procedure                 |
| Fast Random Projection             | `gds.fastRP.mutate` Procedure                              |
|                                    | `gds.fastRP.mutate.estimate` Procedure                     |
|                                    | `gds.fastRP.stats` Procedure                               |
|                                    | `gds.fastRP.stats.estimate` Procedure                      |
|                                    | `gds.fastRP.stream` Procedure                              |
|                                    | `gds.fastRP.stream.estimate` Procedure                     |
|                                    | `gds.fastRP.write` Procedure                               |
|                                    | `gds.fastRP.write.estimate` Procedure                      |
| HashGNN                            | `gds.hashgnn.mutate` Procedure                             |
|                                    | `gds.hashgnn.mutate.estimate` Procedure                    |
|                                    | `gds.hashgnn.stream` Procedure                             |
|                                    | `gds.hashgnn.stream.estimate` Procedure                    |
|                                    | `gds.hashgnn.write` Procedure                              |
|                                    | `gds.hashgnn.write.estimate` Procedure                     |
| HDBSCAN                            | `gds.hdbscan.mutate` Procedure                             |
|                                    | `gds.hdbscan.mutate.estimate` Procedure                    |
|                                    | `gds.hdbscan.stats` Procedure                              |
|                                    | `gds.hdbscan.stats.estimate` Procedure                     |
|                                    | `gds.hdbscan.stream` Procedure                             |
|                                    | `gds.hdbscan.stream.estimate` Procedure                    |
|                                    | `gds.hdbscan.write` Procedure                              |
|                                    | `gds.hdbscan.write.estimate` Procedure                     |
| HITS                               | `gds.hits.mutate` Procedure                                |
|                                    | `gds.hits.mutate.estimate` Procedure                       |
|                                    | `gds.hits.stats` Procedure                                 |
|                                    | `gds.hits.stats.estimate` Procedure                        |
|                                    | `gds.hits.stream` Procedure                                |
|                                    | `gds.hits.stream.estimate` Procedure                       |
|                                    | `gds.hits.write` Procedure                                 |
|                                    | `gds.hits.write.estimate` Procedure                        |
| Influence Maximization - CELF      | `gds.influenceMaximization.celf.mutate` Procedure          |
|                                    | `gds.influenceMaximization.celf.mutate.estimate` Procedure |
|                                    | `gds.influenceMaximization.celf.stats` Procedure           |
|                                    | `gds.influenceMaximization.celf.stats.estimate` Procedure  |
|                                    | `gds.influenceMaximization.celf.stream` Procedure          |
|                                    | `gds.influenceMaximization.celf.stream.estimate` Procedure |
|                                    | `gds.influenceMaximization.celf.write` Procedure           |
|                                    | `gds.influenceMaximization.celf.write.estimate` Procedure  |
| K1Coloring                         | `gds.k1coloring.mutate` Procedure                          |
|                                    | `gds.k1coloring.mutate.estimate` Procedure                 |
|                                    | `gds.k1coloring.stats` Procedure                           |
|                                    | `gds.k1coloring.stats.estimate` Procedure                  |
|                                    | `gds.k1coloring.stream` Procedure                          |
|                                    | `gds.k1coloring.stream.estimate` Procedure                 |
|                                    | `gds.k1coloring.write` Procedure                           |
|                                    | `gds.k1coloring.write.estimate` Procedure                  |
| K-Core Decomposition               | `gds.kcore.stats` Procedure                                |
|                                    | `gds.kcore.stats.estimate` Procedure                       |
|                                    | `gds.kcore.stream` Procedure                               |
|                                    | `gds.kcore.stream.estimate` Procedure                      |
|                                    | `gds.kcore.mutate` Procedure                               |
|                                    | `gds.kcore.mutate.estimate` Procedure                      |
|                                    | `gds.kcore.write` Procedure                                |
|                                    | `gds.kcore.write.estimate` Procedure                       |
| Kmeans                             | `gds.kmeans.mutate` Procedure                              |
|                                    | `gds.kmeans.mutate.estimate` Procedure                     |
|                                    | `gds.kmeans.stats` Procedure                               |
|                                    | `gds.kmeans.stats.estimate` Procedure                      |
|                                    | `gds.kmeans.stream` Procedure                              |
|                                    | `gds.kmeans.stream.estimate` Procedure                     |
|                                    | `gds.kmeans.write` Procedure                               |
|                                    | `gds.kmeans.write.estimate` Procedure                      |
| K-Nearest Neighbors                | `gds.knn.mutate` Procedure                                 |
|                                    | `gds.knn.mutate.estimate` Procedure                        |
|                                    | `gds.knn.stats` Procedure                                  |
|                                    | `gds.knn.stats.estimate` Procedure                         |
|                                    | `gds.knn.stream` Procedure                                 |
|                                    | `gds.knn.stream.estimate` Procedure                        |
|                                    | `gds.knn.write` Procedure                                  |
|                                    | `gds.knn.write.estimate` Procedure                         |
| Filtered KNN                       | `gds.knn.filtered.mutate` Procedure                        |
|                                    | `gds.knn.filtered.mutate.estimate` Procedure               |
|                                    | `gds.knn.filtered.stats` Procedure                         |
|                                    | `gds.knn.filtered.stats.estimate` Procedure                |
|                                    | `gds.knn.filtered.stream` Procedure                        |
|                                    | `gds.knn.filtered.stream.estimate` Procedure               |
|                                    | `gds.knn.filtered.write` Procedure                         |
|                                    | `gds.knn.filtered.write.estimate` Procedure                |
| k-Spanning Tree                    | `gds.kSpanningTree.write` Procedure                        |
| Label Propagation                  | `gds.labelPropagation.mutate` Procedure                    |
|                                    | `gds.labelPropagation.mutate.estimate` Procedure           |
|                                    | `gds.labelPropagation.write` Procedure                     |
|                                    | `gds.labelPropagation.write.estimate` Procedure            |
|                                    | `gds.labelPropagation.stream` Procedure                    |
|                                    | `gds.labelPropagation.stream.estimate` Procedure           |
|                                    | `gds.labelPropagation.stats` Procedure                     |
|                                    | `gds.labelPropagation.stats.estimate` Procedure            |
| Leiden                             | `gds.leiden.mutate` Procedure                              |
|                                    | `gds.leiden.mutate.estimate` Procedure                     |
|                                    | `gds.leiden.stats` Procedure                               |
|                                    | `gds.leiden.stats.estimate` Procedure                      |
|                                    | `gds.leiden.stream` Procedure                              |
|                                    | `gds.leiden.stream.estimate` Procedure                     |
|                                    | `gds.leiden.write` Procedure                               |
|                                    | `gds.leiden.write.estimate` Procedure                      |
| Topological Link Prediction        | `gds.linkprediction.adamicAdar` Function                   |
|                                    | `gds.linkprediction.commonNeighbors` Function              |
|                                    | `gds.linkprediction.preferentialAttachment` Function       |
|                                    | `gds.linkprediction.resourceAllocation` Function           |
|                                    | `gds.linkprediction.sameCommunity` Function                |
|                                    | `gds.linkprediction.totalNeighbors` Function               |
| Local Clustering Coefficient       | `gds.localClusteringCoefficient.stream` Procedure          |
|                                    | `gds.localClusteringCoefficient.stream.estimate` Procedure |
|                                    | `gds.localClusteringCoefficient.stats` Procedure           |
|                                    | `gds.localClusteringCoefficient.stats.estimate` Procedure  |
|                                    | `gds.localClusteringCoefficient.write` Procedure           |
|                                    | `gds.localClusteringCoefficient.write.estimate` Procedure  |
|                                    | `gds.localClusteringCoefficient.mutate` Procedure          |
|                                    | `gds.localClusteringCoefficient.mutate.estimate` Procedure |
| Longest Path for DAGs              | `gds.dag.longestPath.stream` Procedure                     |
| Louvain                            | `gds.louvain.mutate` Procedure                             |
|                                    | `gds.louvain.mutate.estimate` Procedure                    |
|                                    | `gds.louvain.write` Procedure                              |
|                                    | `gds.louvain.write.estimate` Procedure                     |
|                                    | `gds.louvain.stream` Procedure                             |
|                                    | `gds.louvain.stream.estimate` Procedure                    |
|                                    | `gds.louvain.stats` Procedure                              |
|                                    | `gds.louvain.stats.estimate` Procedure                     |
| Maximum flow                       | `gds.maxFlow.mutate` Procedure                             |
|                                    | `gds.maxFlow.mutate.estimate` Procedure                    |
|                                    | `gds.maxFlow.stats` Procedure                              |
|                                    | `gds.maxFlow.stats.estimate` Procedure                     |
|                                    | `gds.maxFlow.stream` Procedure                             |
|                                    | `gds.maxFlow.stream.estimate` Procedure                    |
|                                    | `gds.maxFlow.write` Procedure                              |
|                                    | `gds.maxFlow.write.estimate` Procedure                     |
| Min cost Max-flow                  | `gds.maxFlow.minCost.mutate` Procedure                     |
|                                    | `gds.maxFlow.minCost.mutate.estimate` Procedure            |
|                                    | `gds.maxFlow.minCost.stats` Procedure                      |
|                                    | `gds.maxFlow.minCost.stats.estimate` Procedure             |
|                                    | `gds.maxFlow.minCost.stream` Procedure                     |
|                                    | `gds.maxFlow.minCost.stream.estimate` Procedure            |
|                                    | `gds.maxFlow.minCost.write` Procedure                      |
|                                    | `gds.maxFlow.minCost.write.estimate` Procedure             |
| Approximate Maximum k-cut          | `gds.maxkcut.mutate` Procedure                             |
|                                    | `gds.maxkcut.mutate.estimate` Procedure                    |
|                                    | `gds.maxkcut.stream` Procedure                             |
|                                    | `gds.maxkcut.stream.estimate` Procedure                    |
| Modularity Metric                  | `gds.modularity.stats` Procedure                           |
|                                    | `gds.modularity.stats.estimate` Procedure                  |
|                                    | `gds.modularity.stream` Procedure                          |
|                                    | `gds.modularity.stream.estimate` Procedure                 |
| Modularity Optimization            | `gds.modularityOptimization.mutate` Procedure              |
|                                    | `gds.modularityOptimization.mutate.estimate` Procedure     |
|                                    | `gds.modularityOptimization.stats` Procedure               |
|                                    | `gds.modularityOptimization.stats.estimate` Procedure      |
|                                    | `gds.modularityOptimization.stream` Procedure              |
|                                    | `gds.modularityOptimization.stream.estimate` Procedure     |
|                                    | `gds.modularityOptimization.write` Procedure               |
|                                    | `gds.modularityOptimization.write.estimate` Procedure      |
| Node2Vec                           | `gds.node2vec.mutate` Procedure                            |
|                                    | `gds.node2vec.mutate.estimate` Procedure                   |
|                                    | `gds.node2vec.stream` Procedure                            |
|                                    | `gds.node2vec.stream.estimate` Procedure                   |
|                                    | `gds.node2vec.write` Procedure                             |
|                                    | `gds.node2vec.write.estimate` Procedure                    |
| Node Similarity                    | `gds.nodeSimilarity.mutate` Procedure                      |
|                                    | `gds.nodeSimilarity.mutate.estimate` Procedure             |
|                                    | `gds.nodeSimilarity.write` Procedure                       |
|                                    | `gds.nodeSimilarity.write.estimate` Procedure              |
|                                    | `gds.nodeSimilarity.stream` Procedure                      |
|                                    | `gds.nodeSimilarity.stream.estimate` Procedure             |
|                                    | `gds.nodeSimilarity.stats` Procedure                       |
|                                    | `gds.nodeSimilarity.stats.estimate` Procedure              |
| Filtered NodeSimilarity            | `gds.nodeSimilarity.filtered.mutate` Procedure             |
|                                    | `gds.nodeSimilarity.filtered.mutate.estimate` Procedure    |
|                                    | `gds.nodeSimilarity.filtered.stats` Procedure              |
|                                    | `gds.nodeSimilarity.filtered.stats.estimate` Procedure     |
|                                    | `gds.nodeSimilarity.filtered.stream` Procedure             |
|                                    | `gds.nodeSimilarity.filtered.stream.estimate` Procedure    |
|                                    | `gds.nodeSimilarity.filtered.write` Procedure              |
|                                    | `gds.nodeSimilarity.filtered.write.estimate` Procedure     |
| PageRank                           | `gds.pageRank.mutate` Procedure                            |
|                                    | `gds.pageRank.mutate.estimate` Procedure                   |
|                                    | `gds.pageRank.write` Procedure                             |
|                                    | `gds.pageRank.write.estimate` Procedure                    |
|                                    | `gds.pageRank.stream` Procedure                            |
|                                    | `gds.pageRank.stream.estimate` Procedure                   |
|                                    | `gds.pageRank.stats` Procedure                             |
|                                    | `gds.pageRank.stats.estimate` Procedure                    |
| Prize Collecting Steiner Tree      | `gds.prizeSteinerTree.mutate` Procedure                    |
|                                    | `gds.prizeSteinerTree.mutate.estimate` Procedure           |
|                                    | `gds.prizeSteinerTree.stats` Procedure                     |
|                                    | `gds.prizeSteinerTree.stats.estimate` Procedure            |
|                                    | `gds.prizeSteinerTree.stream` Procedure                    |
|                                    | `gds.prizeSteinerTree.stream.estimate` Procedure           |
|                                    | `gds.prizeSteinerTree.write` Procedure                     |
|                                    | `gds.prizeSteinerTree.write.estimate` Procedure            |
| Random Walk                        | `gds.randomWalk.mutate` Procedure                          |
|                                    | `gds.randomWalk.mutate.estimate` Procedure                 |
|                                    | `gds.randomWalk.stats` Procedure                           |
|                                    | `gds.randomWalk.stats.estimate` Procedure                  |
|                                    | `gds.randomWalk.stream` Procedure                          |
|                                    | `gds.randomWalk.stream.estimate` Procedure                 |
| Scale Properties                   | `gds.scaleProperties.mutate` Procedure                     |
|                                    | `gds.scaleProperties.mutate.estimate` Procedure            |
|                                    | `gds.scaleProperties.stream` Procedure                     |
|                                    | `gds.scaleProperties.stream.estimate` Procedure            |
|                                    | `gds.scaleProperties.stats` Procedure                      |
|                                    | `gds.scaleProperties.stats.estimate` Procedure             |
|                                    | `gds.scaleProperties.write` Procedure                      |
|                                    | `gds.scaleProperties.write.estimate` Procedure             |
| Strongly Connected Components      | `gds.scc.mutate` Procedure                                 |
|                                    | `gds.scc.mutate.estimate` Procedure                        |
|                                    | `gds.scc.stats` Procedure                                  |
|                                    | `gds.scc.stats.estimate` Procedure                         |
|                                    | `gds.scc.stream` Procedure                                 |
|                                    | `gds.scc.stream.estimate` Procedure                        |
|                                    | `gds.scc.write` Procedure                                  |
|                                    | `gds.scc.write.estimate` Procedure                         |
| Shortest Path AStar                | `gds.shortestPath.astar.stream` Procedure                  |
|                                    | `gds.shortestPath.astar.stream.estimate` Procedure         |
|                                    | `gds.shortestPath.astar.write` Procedure                   |
|                                    | `gds.shortestPath.astar.write.estimate` Procedure          |
|                                    | `gds.shortestPath.astar.mutate` Procedure                  |
|                                    | `gds.shortestPath.astar.mutate.estimate` Procedure         |
| Shortest Path Dijkstra             | `gds.shortestPath.dijkstra.stream` Procedure               |
|                                    | `gds.shortestPath.dijkstra.stream.estimate` Procedure      |
|                                    | `gds.shortestPath.dijkstra.write` Procedure                |
|                                    | `gds.shortestPath.dijkstra.write.estimate` Procedure       |
|                                    | `gds.shortestPath.dijkstra.mutate` Procedure               |
|                                    | `gds.shortestPath.dijkstra.mutate.estimate` Procedure      |
| Shortest Paths Yens                | `gds.shortestPath.yens.stream` Procedure                   |
|                                    | `gds.shortestPath.yens.stream.estimate` Procedure          |
|                                    | `gds.shortestPath.yens.write` Procedure                    |
|                                    | `gds.shortestPath.yens.write.estimate` Procedure           |
|                                    | `gds.shortestPath.yens.mutate` Procedure                   |
|                                    | `gds.shortestPath.yens.mutate.estimate` Procedure          |
| Similarity functions               | `gds.similarity.cosine` Function                           |
|                                    | `gds.similarity.euclidean` Function                        |
|                                    | `gds.similarity.euclideanDistance` Function                |
|                                    | `gds.similarity.jaccard` Function                          |
|                                    | `gds.similarity.overlap` Function                          |
|                                    | `gds.similarity.pearson` Function                          |
| Speaker-Listener Label Propagation | `gds.sllpa.mutate` Procedure                               |
|                                    | `gds.sllpa.mutate.estimate` Procedure                      |
|                                    | `gds.sllpa.stats` Procedure                                |
|                                    | `gds.sllpa.stats.estimate` Procedure                       |
|                                    | `gds.sllpa.stream` Procedure                               |
|                                    | `gds.sllpa.stream.estimate` Procedure                      |
|                                    | `gds.sllpa.write` Procedure                                |
|                                    | `gds.sllpa.write.estimate` Procedure                       |
| Spanning Tree                      | `gds.spanningTree.mutate` Procedure                        |
|                                    | `gds.spanningTree.mutate.estimate` Procedure               |
|                                    | `gds.spanningTree.stats` Procedure                         |
|                                    | `gds.spanningTree.stats.estimate` Procedure                |
|                                    | `gds.spanningTree.stream` Procedure                        |
|                                    | `gds.spanningTree.stream.estimate` Procedure               |
|                                    | `gds.spanningTree.write` Procedure                         |
|                                    | `gds.spanningTree.write.estimate` Procedure                |
| Split Relationships                | `gds.splitRelationships.mutate` Procedure                  |
|                                    | `gds.splitRelationships.mutate.estimate` Procedure         |
| Minimum Directed Steiner Tree      | `gds.steinerTree.mutate` Procedure                         |
|                                    | `gds.steinerTree.mutate.estimate` Procedure                |
|                                    | `gds.steinerTree.stats` Procedure                          |
|                                    | `gds.steinerTree.stats.estimate` Procedure                 |
|                                    | `gds.steinerTree.stream` Procedure                         |
|                                    | `gds.steinerTree.stream.estimate` Procedure                |
|                                    | `gds.steinerTree.write` Procedure                          |
|                                    | `gds.steinerTree.write.estimate` Procedure                 |
| Triangle Count                     | `gds.triangleCount.stream` Procedure                       |
|                                    | `gds.triangleCount.stream.estimate` Procedure              |
|                                    | `gds.triangleCount.stats` Procedure                        |
|                                    | `gds.triangleCount.stats.estimate` Procedure               |
|                                    | `gds.triangleCount.write` Procedure                        |
|                                    | `gds.triangleCount.write.estimate` Procedure               |
|                                    | `gds.triangleCount.mutate` Procedure                       |
|                                    | `gds.triangleCount.mutate.estimate` Procedure              |
| Triangle Listing                   | `gds.triangles` Procedure                                  |
| Weakly Connected Components        | `gds.wcc.mutate` Procedure                                 |
|                                    | `gds.wcc.mutate.estimate` Procedure                        |
|                                    | `gds.wcc.write` Procedure                                  |
|                                    | `gds.wcc.write.estimate` Procedure                         |
|                                    | `gds.wcc.stream` Procedure                                 |
|                                    | `gds.wcc.stream.estimate` Procedure                        |
|                                    | `gds.wcc.stats` Procedure                                  |
|                                    | `gds.wcc.stats.estimate` Procedure                         |

> Beta tier

Table 2. List of all beta algorithms in the GDS library.

| Algorithm name | Operation                                      |
| -------------- | ---------------------------------------------- |
| GraphSAGE      | `gds.beta.graphSage.stream` Procedure          |
|                | `gds.beta.graphSage.stream.estimate` Procedure |
|                | `gds.beta.graphSage.mutate` Procedure          |
|                | `gds.beta.graphSage.mutate.estimate` Procedure |
|                | `gds.beta.graphSage.write` Procedure           |
|                | `gds.beta.graphSage.write.estimate` Procedure  |
|                | `gds.beta.graphSage.train` Procedure           |
|                | `gds.beta.graphSage.train.estimate` Procedure  |


### Maching Learning/机器学习

> Pipeline Catalog/流水线目录

Table 1. List of all pipeline catalog operations in the GDS library.

| Description                   | Operation                       |
| ----------------------------- | ------------------------------- |
| Check if a pipeline exists    | `gds.pipeline.exists` Procedure |
| Remove a pipeline from memory | `gds.pipeline.drop` Procedure   |
| List pipelines                | `gds.pipeline.list` Procedure   |

> Model Catalog/模型目录

Table 2. List of all model catalog operations in the GDS library.

| Description              | Operation                                        |
| ------------------------ | ------------------------------------------------ |
| List models              | `gds.model.list` Procedure                       |
| Check if a model exists  | `gds.model.exists` Procedure                     |
| Drop a model from memory | `gds.model.drop` Procedure                       |
| Store a model            | `gds.model.store` Procedure Enterprise Edition   |
| Load a stored model      | `gds.model.load` Procedure Enterprise Edition    |
| Delete a stored model    | `gds.model.delete` Procedure Enterprise Edition  |
| Publish a model          | `gds.model.publish` Procedure Enterprise Edition |

> Pipelines/流水线

>> Beta tier

Table 3. List of all beta machine learning pipelines operations in the GDS library.

| Algorithm name               | Operation                                                                |
| ---------------------------- | ------------------------------------------------------------------------ |
| Link Prediction Pipeline     | `gds.beta.pipeline.linkPrediction.create` Procedure                      |
|                              | `gds.beta.pipeline.linkPrediction.addNodeProperty` Procedure             |
|                              | `gds.beta.pipeline.linkPrediction.addFeature` Procedure                  |
|                              | `gds.beta.pipeline.linkPrediction.addLogisticRegression` Procedure       |
|                              | `gds.beta.pipeline.linkPrediction.addRandomForest` Procedure             |
|                              | `gds.beta.pipeline.linkPrediction.configureSplit` Procedure              |
|                              | `gds.beta.pipeline.linkPrediction.train` Procedure                       |
|                              | `gds.beta.pipeline.linkPrediction.train.estimate` Procedure              |
|                              | `gds.beta.pipeline.linkPrediction.predict.mutate` Procedure              |
|                              | `gds.beta.pipeline.linkPrediction.predict.mutate.estimate` Procedure     |
|                              | `gds.beta.pipeline.linkPrediction.predict.stream` Procedure              |
|                              | `gds.beta.pipeline.linkPrediction.predict.stream.estimate` Procedure     |
| Node Classification Pipeline | `gds.beta.pipeline.nodeClassification.create` Procedure                  |
|                              | `gds.beta.pipeline.nodeClassification.addNodeProperty` Procedure         |
|                              | `gds.beta.pipeline.nodeClassification.selectFeatures` Procedure          |
|                              | `gds.beta.pipeline.nodeClassification.addLogisticRegression` Procedure   |
|                              | `gds.beta.pipeline.nodeClassification.addRandomForest` Procedure         |
|                              | `gds.beta.pipeline.nodeClassification.configureSplit` Procedure          |
|                              | `gds.beta.pipeline.nodeClassification.train` Procedure                   |
|                              | `gds.beta.pipeline.nodeClassification.train.estimate` Procedure          |
|                              | `gds.beta.pipeline.nodeClassification.predict.mutate` Procedure          |
|                              | `gds.beta.pipeline.nodeClassification.predict.mutate.estimate` Procedure |
|                              | `gds.beta.pipeline.nodeClassification.predict.stream` Procedure          |
|                              | `gds.beta.pipeline.nodeClassification.predict.stream.estimate` Procedure |
|                              | `gds.beta.pipeline.nodeClassification.predict.write` Procedure           |
|                              | `gds.beta.pipeline.nodeClassification.predict.write.estimate` Procedure  |

>> Alpha tier

Table 4. List of all alpha machine learning pipelines operations in the GDS library.

| Algorithm name               | Operation                                                             |
| ---------------------------- | --------------------------------------------------------------------- |
| Link Prediction Pipeline     | `gds.alpha.pipeline.linkPrediction.addMLP` Procedure                  |
|                              | `gds.alpha.pipeline.linkPrediction.configureAutoTuning` Procedure     |
| Node Classification Pipeline | `gds.alpha.pipeline.nodeClassification.addMLP` Procedure              |
|                              | `gds.alpha.pipeline.nodeClassification.configureAutoTuning` Procedure |
| Node Regression Pipeline     | `gds.alpha.pipeline.nodeRegression.create` Procedure                  |
|                              | `gds.alpha.pipeline.nodeRegression.addNodeProperty` Procedure         |
|                              | `gds.alpha.pipeline.nodeRegression.selectFeatures` Procedure          |
|                              | `gds.alpha.pipeline.nodeRegression.configureAutoTuning` Procedure     |
|                              | `gds.alpha.pipeline.nodeRegression.configureSplit` Procedure          |
|                              | `gds.alpha.pipeline.nodeRegression.addLinearRegression` Procedure     |
|                              | `gds.alpha.pipeline.nodeRegression.addRandomForest` Procedure         |
|                              | `gds.alpha.pipeline.nodeRegression.train` Procedure                   |
|                              | `gds.alpha.pipeline.nodeRegression.predict.stream` Procedure          |
|                              | `gds.alpha.pipeline.nodeRegression.predict.mutate` Procedure          |

> Node embeddings/节点嵌入

>> Production-quality tier

Table 5. List of all production-quality node embedding algorithms in the GDS library.

| Algorithm name         | Operation                                |
| ---------------------- | ---------------------------------------- |
| Fast Random Projection | `gds.fastRP.mutate` Procedure            |
|                        | `gds.fastRP.mutate.estimate` Procedure   |
|                        | `gds.fastRP.stats` Procedure             |
|                        | `gds.fastRP.stats.estimate` Procedure    |
|                        | `gds.fastRP.stream` Procedure            |
|                        | `gds.fastRP.stream.estimate` Procedure   |
|                        | `gds.fastRP.write` Procedure             |
|                        | `gds.fastRP.write.estimate` Procedure    |
| HashGNN                | `gds.hashgnn.mutate` Procedure           |
|                        | `gds.hashgnn.mutate.estimate` Procedure  |
|                        | `gds.hashgnn.stream` Procedure           |
|                        | `gds.hashgnn.stream.estimate` Procedure  |
| Node2Vec               | `gds.node2vec.mutate` Procedure          |
|                        | `gds.node2vec.mutate.estimate` Procedure |
|                        | `gds.node2vec.stream` Procedure          |
|                        | `gds.node2vec.stream.estimate` Procedure |
|                        | `gds.node2vec.write` Procedure           |
|                        | `gds.node2vec.write.estimate` Procedure  |

>> Beta tier

Table 6. List of all beta node embedding algorithms in the GDS library.

| Algorithm name | Operation                                      |
| -------------- | ---------------------------------------------- |
| GraphSAGE      | `gds.beta.graphSage.stream` Procedure          |
|                | `gds.beta.graphSage.stream.estimate` Procedure |
|                | `gds.beta.graphSage.mutate` Procedure          |
|                | `gds.beta.graphSage.mutate.estimate` Procedure |
|                | `gds.beta.graphSage.write` Procedure           |
|                | `gds.beta.graphSage.write.estimate` Procedure  |
|                | `gds.beta.graphSage.train` Procedure           |
|                | `gds.beta.graphSage.train.estimate` Procedure  |

### Additional Operations/其他操作

Table 1. List of all additional operations.

| Description                                                      | Operation                            |
| ---------------------------------------------------------------- | ------------------------------------ |
| The version of the installed GDS                                 | `gds.version` Procedure              |
|                                                                  | `gds.version` Function               |
| List all operations in GDS                                       | `gds.list` Procedure                 |
| List logged progress                                             | `gds.listProgress` Procedure         |
| List warnings                                                    | `gds.userLog` Procedure              |
| The license state of the installed GDS                           | `gds.license.state` Procedure        |
|                                                                  | `gds.isLicensed` Function            |
| Node id functions                                                | `gds.util.asNode` Function           |
|                                                                  | `gds.util.asNodes` Function          |
| Numeric Functions                                                | `gds.util.NaN` Function              |
|                                                                  | `gds.util.infinity` Function         |
|                                                                  | `gds.util.isFinite` Function         |
|                                                                  | `gds.util.isInfinite` Function       |
| Accessing a node property in a named graph                       | `gds.util.nodeProperty` Function     |
| One Hot Encoding                                                 | `gds.util.oneHotEncoding` Function   |
| Status of the system                                             | `gds.debug.sysInfo` Procedure        |
| Monitoring                                                       | `gds.debug.arrow` Procedure          |
| Get an overview of the system’s workload and available resources | `gds.systemMonitor` Procedure        |
| Detailed memory footprint reporting                              | `gds.memory.list` Procedure          |
| Memory footprint summary                                         | `gds.memory.summary` Procedure       |
| Back-up graphs and models to disk                                | `gds.backup` Procedure               |
| Restore persisted graphs and models to memory                    | `gds.restore` Procedure              |
| List configured defaults                                         | `gds.config.defaults.list` Procedure |
| Configure a default                                              | `gds.config.defaults.set` Procedure  |
| List configured limits                                           | `gds.config.limits.list` Procedure   |
| Configure a limit                                                | `gds.config.limits.set` Procedure    |


### Configuration Settings/配置设置

This page describes the available configuration settings in GDS. Refer to The [neo4j.conf](https://neo4j.com/docs/operations-manual/current/configuration/neo4j-conf/#neo4j-conf) file for details on how to use configuration settings.



All settings
| Setting                                  | Description                                                                                                              | Misc               |
| ---------------------------------------- | ------------------------------------------------------------------------------------------------------------------------ | ------------------ |
| gds.arrow.abortion_timeout               | The maximum time to wait for the next command before aborting the import process.                                        | Enterprise Edition |
| gds.arrow.advertised_listen_address      | Address that clients should use to connect to the GDS Arrow Flight Server.                                               | Enterprise Edition |
| gds.arrow.batch_size                     | The batch size used for arrow property export.                                                                           | Enterprise Edition |
| gds.arrow.enabled                        | Enable the GDS Arrow Flight Server.                                                                                      | Enterprise Edition |
| gds.arrow.encryption.never               | Never activate server-side encryption for the GDS Arrow Flight Server.                                                   | Enterprise Edition |
| gds.arrow.listen_address                 | Address the GDS Arrow Flight Server should bind to.                                                                      | Enterprise Edition |
| gds.memory.arrow.max_size                | The maximum amount of memory in bytes the GDS Arrow Flight Server can allocate.                                          | Enterprise Edition |
| gds.cluster.tx.max.size                  | Set the maximum transaction size for GDS write back when running in Neo4j Cluster.                                       | Enterprise Edition |
| gds.cluster.tx.min.size                  | Set the minimum transaction size for GDS write back when running in Neo4j Cluster.                                       | Enterprise Edition |
| gds.enterprise.license_file              | Sets the location of the file that contains the Neo4j Graph Data Science library license key.                            |                    |
| gds.export.location                      | Sets the export location for file based exports.                                                                         | Enterprise Edition |
| gds.model.store_location                 | Sets the location where persisted models are stored.                                                                     | Enterprise Edition |
| gds.progress_tracking_enabled            | Enable progress logging tracking.                                                                                        |                    |
| gds.progress_tracking_retention_period   | Duration of retaining completed jobs. Retained jobs will be included in the output of `gds.listProgress(<jobId>, true)`. |                    |
| gds.validate_using_max_memory_estimation | Use maximum memory estimation in procedure memory guard.                                                                 |                    |


- gds.arrow.abortion_timeout

| Key           | Value                                                                                      |
| ------------- | ------------------------------------------------------------------------------------------ |
| Description   | The maximum time to wait for the next command before aborting the import process.          |
| Default Value | `10m`                                                                                      |
| Valid Values  | A duration (Valid units are: `ns`, `μs`, `ms`, `s`, `m`, `h` and `d` default unit is `s`). |
| Dynamic       | `false`                                                                                    |


- gds.arrow.advertised_listen_address

| Key           | Value                                                                                                                                              |
| ------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| Description   | Address that clients should use to connect to the GDS Arrow Flight Server.                                                                         |
| Default Value | `:8491`                                                                                                                                            |
| Valid Values  | A socket address in the format `hostname:port`, `hostname` or `:port`. If missing port or hostname it is acquired from `gds.arrow.listen_address`. |
| Dynamic       | `false`                                                                                                                                            |

- - gds.arrow.batch_size

| Key           | Value                                          |
| ------------- | ---------------------------------------------- |
| Description   | The batch size used for arrow property export. |
| Default Value | `10000`                                        |
| Valid Values  | An integer.                                    |
| Dynamic       | `true`                                         |

- gds.arrow.enabled

| Key           | Value                               |
| ------------- | ----------------------------------- |
| Description   | Enable the GDS Arrow Flight Server. |
| Default Value | `false`                             |
| Valid Values  | A boolean.                          |
| Dynamic       | `false`                             |

- gds.arrow.encryption.never

| Key           | Value                                                                  |
| ------------- | ---------------------------------------------------------------------- |
| Description   | Never activate server-side encryption for the GDS Arrow Flight Server. |
| Default Value | `false`                                                                |
| Valid Values  | A boolean.                                                             |
| Dynamic       | `false`                                                                |

- gds.arrow.listen_address

| Key           | Value                                                                  |
| ------------- | ---------------------------------------------------------------------- |
| Description   | Address the GDS Arrow Flight Server should bind to.                    |
| Default Value | `localhost:8491`                                                       |
| Valid Values  | A socket address in the format `hostname:port`, `hostname` or `:port`. |
| Dynamic       | `false`                                                                |

- gds.memory.arrow.max_size

| Key           | Value                                                                           |
| ------------- | ------------------------------------------------------------------------------- |
| Description   | The maximum amount of memory in bytes the GDS Arrow Flight Server can allocate. |
| Default Value | `Long.MAX_VALUE`                                                                |
| Valid Values  | A long.                                                                         |
| Dynamic       | `false`                                                                         |

- gds.cluster.tx.max.size

| Key           | Value                                                                                    |
| ------------- | ---------------------------------------------------------------------------------------- |
| Description   | Set the maximum transaction size for GDS write back when running in Neo4j Cluster.       |
| Default Value | `100000`                                                                                 |
| Valid Values  | An integer, must be set greater than or equal to the value of `gds.cluster.tx.min.size`. |
| Dynamic       | `false`                                                                                  |

- gds.cluster.tx.min.size

| Key           | Value                                                                              |
| ------------- | ---------------------------------------------------------------------------------- |
| Description   | Set the minimum transaction size for GDS write back when running in Neo4j Cluster. |
| Default Value | `10000`                                                                            |
| Valid Values  | An integer.                                                                        |
| Dynamic       | `false`                                                                            |

- gds.enterprise.license_file

| Key           | Value                                                                                         |
| ------------- | --------------------------------------------------------------------------------------------- |
| Description   | Sets the location of the file that contains the Neo4j Graph Data Science library license key. |
| Default Value | `No Value`                                                                                    |
| Valid Values  | An absolute path.                                                                             |
| Dynamic       | `false`                                                                                       |

- gds.export.location

| Key           | Value                                            |
| ------------- | ------------------------------------------------ |
| Description   | Sets the export location for file based exports. |
| Default Value | `No Value`                                       |
| Valid Values  | An absolute path.                                |
| Dynamic       | `false`                                          |

- gds.model.store_location

| Key           | Value                                                |
| ------------- | ---------------------------------------------------- |
| Description   | Sets the location where persisted models are stored. |
| Default Value | `No Value`                                           |
| Valid Values  | An absolute path.                                    |
| Dynamic       | `false`                                              |

- gds.progress_tracking_enabled

| Key           | Value                             |
| ------------- | --------------------------------- |
| Description   | Enable progress logging tracking. |
| Default Value | `true`                            |
| Valid Values  | A boolean.                        |
| Dynamic       | `false`                           |

- gds.progress_tracking_retention_period

| Key           | Value                                                                                                          |
| ------------- | -------------------------------------------------------------------------------------------------------------- |
| Description   | The duration for which completed progress tracking jobs are retained. This includes failed and successful ones |
| Default Value | `0s`                                                                                                           |
| Valid Values  | A duration (Valid units are: `ns`, `μs`, `ms`, `s`, `m`, `h` and `d` default unit is `s`).                     |
| Dynamic       | `false`                                                                                                        |

- gds.validate_using_max_memory_estimation

| Key           | Value                                                    |
| ------------- | -------------------------------------------------------- |
| Description   | Use maximum memory estimation in procedure memory guard. |
| Default Value | `false`                                                  |
| Valid Values  | A boolean.                                               |
| Dynamic       | `false`                                                  |

# GDS Python Client
* https://neo4j.com/docs/graph-data-science-client/current/

## The graph object
## Running algorithms
## Machine learning pipelines
## Model objects from the model catalog
## Datasets
## Relationship embedding models
## Coordinate parallel transactions
## Visualization
## Tutorials

# See Also

- Neo4j Graph Data Science (GDS) 库包含哪些算法?
```markdown
Neo4j Graph Data Science (GDS) 库包含超过 65 种算法，主要分为六大核心类别，涵盖了从基础路径查找到高级图机器学习的各种需求： [1] 
## 1. 路径查找与搜索 (Path Finding & Search)
这些算法用于寻找节点之间的最佳路径或评估路线的可用性。 [2, 3] 

* 代表算法：Dijkstra（最短路径）、A*、Yen's（K-最短路径）、Breadth First Search (BFS)、Depth First Search (DFS)、Random Walk（随机游走）以及 Maximum Flow（最大流）。
* 应用场景：物理物流优化、最低成本路由、网络流量分析。 [4, 5, 6] 

## 2. 中心性算法 (Centrality)
用于识别图中哪些节点最为“重要”或具有影响力。 [3, 7] 

* 代表算法：PageRank、Betweenness Centrality（介数中心性）、Degree Centrality（度中心性）、Closeness Centrality（紧密中心性）及 Eigenvector Centrality。
* 应用场景：识别社交媒体上的影响力人物、分析基础设施的关键节点。 [4, 6, 8] 

## 3. 社区检测 (Community Detection)
用于发现图中的自然聚类、分区或紧密联系的群体。 [2, 3] 

* 代表算法：Louvain（鲁汶算法）、Label Propagation (LPA)、Weakly Connected Components (WCC)、Strongly Connected Components (SCC) 以及 Triangle Count。
* 应用场景：反欺诈团伙识别、生物网络功能模块发现。 [9, 10, 11] 

## 4. 相似度算法 (Similarity)
根据节点的邻居结构或属性计算节点对之间的相似程度。 [3, 12] 

* 代表算法：Node Similarity（基于 Jaccard 或 Overlap）、K-Nearest Neighbors (KNN)。
* 内置函数：余弦相似度 (Cosine)、欧几里得距离 (Euclidean)、皮尔逊相关系数 (Pearson)。
* 应用场景：基于相似用户或商品的推荐系统。 [6, 12, 13, 14, 15] 

## 5. 链接预测 (Link Prediction)
预测图中目前不存在但未来可能形成的连接。 [3, 16] 

* 代表算法：Adamic Adar、Common Neighbors（共同邻居）、Preferential Attachment（优先连接）、Resource Allocation。
* 应用场景：社交好友推荐、知识图谱补全。 [11, 16, 17] 

## 6. 节点嵌入 (Node Embeddings)
将复杂的图结构和属性转化为固定长度的数值向量，供下游机器学习模型使用。 [18, 19] 

* 代表算法：Fast Random Projection (FastRP)、Node2Vec、GraphSAGE。
* 应用场景：作为机器学习特征输入，用于节点分类或异常检测。 [17, 20, 21] 

此外，GDS 还提供 机器学习训练管道 (ML Pipelines)，支持端到端的节点分类、回归和链接预测工作流。 [1, 22] 
您是否需要针对某个特定类别的算法（如社区检测或嵌入算法）了解其具体的调用命令或参数配置？

[1] [https://juejin.cn](https://juejin.cn/post/7483462574869577740)
[2] [https://neo4j.com](https://neo4j.com/graphacademy/training-iga-40/03-iga-40-intro-to-gds/)
[3] [https://neo4j.ac.cn](https://neo4j.ac.cn/docs/spark/current/gds/#:~:text=%E4%BD%BF%E7%94%A8Neo4j%20GDS%20%E8%BF%9B%E8%A1%8C%E6%95%B0%E6%8D%AE%E7%A7%91%E5%AD%A6.%20Neo4j%20%E5%9B%BE%E6%95%B0%E6%8D%AE%E7%A7%91%E5%AD%A6%28GDS%29%20%E8%AE%A9%E6%95%B0%E6%8D%AE%E7%A7%91%E5%AD%A6%E5%AE%B6%E8%83%BD%E5%A4%9F%E5%88%A9%E7%94%A8%E5%BC%BA%E5%A4%A7%E7%9A%84%E5%9B%BE%E7%AE%97%E6%B3%95%E3%80%82%E5%AE%83%E6%8F%90%E4%BE%9B%E4%BA%86%E6%97%A0%E7%9B%91%E7%9D%A3%E6%9C%BA%E5%99%A8%E5%AD%A6%E4%B9%A0%28ML%29%20%E6%96%B9%E6%B3%95%E5%92%8C%E5%90%AF%E5%8F%91%E5%BC%8F%E7%AE%97%E6%B3%95%EF%BC%8C%E7%94%A8%E4%BA%8E%E5%AD%A6%E4%B9%A0%E5%92%8C%E6%8F%8F%E8%BF%B0%E5%9B%BE%E7%9A%84%E6%8B%93%E6%89%91%E7%BB%93%E6%9E%84%E3%80%82GDS,GDS%20%E7%AE%97%E6%B3%95%E5%88%86%E4%B8%BA%E4%BA%94%E7%BB%84%EF%BC%9A.%20%E7%A4%BE%E5%8C%BA%E6%A3%80%E6%B5%8B%EF%BC%9A%E7%94%A8%E4%BA%8E%E6%A3%80%E6%B5%8B%E7%BB%84%E9%9B%86%E7%BE%A4%E5%92%8C%E5%88%86%E5%8C%BA%E9%80%89%E9%A1%B9%E3%80%82%20%E4%B8%AD%E5%BF%83%E6%80%A7%EF%BC%9A%E7%94%A8%E4%BA%8E%E8%AE%A1%E7%AE%97%E5%9B%BE%E4%B8%AD%E8%8A%82%E7%82%B9%E7%9A%84%E9%87%8D%E8%A6%81%E6%80%A7%E3%80%82%20%E6%8B%93%E6%89%91%E9%93%BE%E6%8E%A5%E9%A2%84%E6%B5%8B%EF%BC%9A%E7%94%A8%E4%BA%8E%E4%BC%B0%E8%AE%A1%E8%8A%82%E7%82%B9%E5%BD%A2%E6%88%90%E5%85%B3%E7%B3%BB%E7%9A%84%E5%8F%AF%E8%83%BD%E6%80%A7%E3%80%82%20%E7%9B%B8%E4%BC%BC%E6%80%A7%EF%BC%9A%E7%94%A8%E4%BA%8E%E8%AF%84%E4%BC%B0%E8%8A%82%E7%82%B9%E5%AF%B9%E7%9A%84%E7%9B%B8%E4%BC%BC%E6%80%A7%E3%80%82%20%E8%B7%AF%E5%BE%84%E6%9F%A5%E6%89%BE%E4%B8%8E%E6%90%9C%E7%B4%A2%EF%BC%9A%E7%94%A8%E4%BA%8E%E6%9F%A5%E6%89%BE%E6%9C%80%E4%BD%B3%E8%B7%AF%E5%BE%84%E3%80%81%E8%AF%84%E4%BC%B0%E8%B7%AF%E7%BA%BF%E5%8F%AF%E7%94%A8%E6%80%A7%E7%AD%89%E3%80%82)
[4] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/operations-reference/algorithm-references/)
[5] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/algorithms/pathfinding/)
[6] [https://neo4j.com](https://neo4j.com/blog/graph-data-science/evolving-your-application-of-gds-technology/)
[7] [https://neo4j.com](https://neo4j.com/docs/bloom-user-guide/current/bloom-tutorial/gds-integration/)
[8] [https://neo4j.com](https://neo4j.com/blog/developer/using-neo4j-graph-data-science-in-python-to-improve-machine-learning-models/)
[9] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/algorithms/)
[10] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/)
[11] [https://neo4j.com](https://neo4j.com/blog/news/announcing-neo4j-for-graph-data-science/)
[12] [https://zhuanlan.zhihu.com](https://zhuanlan.zhihu.com/p/681199537)
[13] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/migration-gds-1-to-gds-2/migration-algorithms/#:~:text=Table_title:%20Alpha%20similarity%20algorithms%20Table_content:%20header:%20%7C,%7C%201.x:%20gds.alpha.similarity.jaccard%20%7C%202.x:%20gds.similarity.jaccard%20%7C)
[14] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/algorithms/node-similarity/)
[15] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/migration-gds-1-to-gds-2/migration-algorithms/#:~:text=Table_title:%20Alpha%20similarity%20algorithms%20Table_content:%20header:%20%7C,%7C%201.x:%20gds.alpha.similarity.jaccard%20%7C%202.x:%20gds.similarity.jaccard%20%7C)
[16] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/algorithms/linkprediction/)
[17] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/)
[18] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/machine-learning/node-embeddings/#:~:text=Node%20embedding%20algorithms%20compute%20low%2Ddimensional%20vector%20representations,library%20contains%20the%20following%20node%20embedding%20algorithms:)
[19] [https://www.tpisoftware.com](https://www.tpisoftware.com/tpu/articleDetails/2650#:~:text=What%27s%20an%20embedding%20%C2%B7%20Graph%20Embedding%20%C2%B7,%E9%80%99%E6%84%8F%E5%91%B3%E8%91%97%E6%82%A8%E5%8F%AF%E4%BB%A5%E6%8D%95%E7%8D%B2%E5%9C%96%E5%BD%A2%E7%9A%84%E8%A4%87%E9%9B%9C%E6%80%A7%E5%92%8C%E7%B5%90%E6%A7%8B%EF%BC%8C%E4%B8%A6%E5%B0%87%E5%85%B6%E8%BD%89%E6%8F%9B%E7%82%BA%E7%94%A8%E6%96%BC%E5%90%84%E7%A8%AEML%20%E9%A0%90%E6%B8%AC%E3%80%82%20%E6%9C%AC%E6%96%87%E7%AB%A0%E5%B0%87%E6%9C%83%E7%B0%A1%E5%96%AE%E4%BB%8B%E7%B4%B9Graph%20Embedding%EF%BC%8C%E4%B8%A6%E4%B8%94%E5%AF%A6%E4%BD%9CGraph%20Embedding%E5%9C%A8Neo4j%E4%B8%8A%28%E4%BD%BF%E7%94%A8MIND%20News%20Dataset%29)
[20] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/getting-started/fastrp-knn-example/)
[21] [https://neo4j.com](https://neo4j.com/videos/enhancing-benefit-adjudication-through-graph-node-embedding-clustering-and-outlier-detection/)
[22] [https://neo4j.com](https://neo4j.com/docs/graph-data-science/current/machine-learning/linkprediction-pipelines/link-prediction/)
```

