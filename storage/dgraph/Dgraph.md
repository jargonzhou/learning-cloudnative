# Dgraph
* https://github.com/dgraph-io/dgraph

License: Apache 2.0, Language: Go

Dgraph is a horizontally scalable and distributed GraphQL database with a graph backend. It provides ACID transactions, consistent replication, and linearizable reads. It's built from the ground up to perform a rich set of queries. Being a native GraphQL database, it tightly controls how the data is arranged on disk to optimize for query performance and throughput, reducing disk seeks and network calls in a cluster.

Dgraph's goal is to provide Google production-level scale and throughput, with low enough latency to serve real-time user queries over terabytes of structured data. Dgraph supports GraphQL query syntax, and responds in JSON and Protocol Buffers over GRPC and HTTP. Dgraph is written using the Go Programming Language.

# Architecture
* https://docs.dgraph.io/installation/dgraph-architecture

Dgraph cluster consists of different nodes (Zero, Alpha & Ratel), and each node serves a different purpose.

- **Dgraph Zero** controls the Dgraph cluster, assigns servers to a group, and re-balances data between server groups.
- **Dgraph Alpha** hosts predicates and indexes. Predicates are either the properties associated with a node or the relationship between two nodes. Indexes are the tokenizers that can be associated with the predicates to enable filtering using appropriate functions.
- **Ratel** serves the UI to run queries, mutations & altering schema.

Default ports used by different nodes[^1][^2]:

| Dgraph Node Type | gRPC internal private | gRPC external private | gRPC external public | HTTP external private | HTTP external public |
| :--------------- | :-------------------- | :-------------------- | :------------------- | :-------------------- | :------------------- |
| zero             | 50801                 | 50801                 |                      | 60802                 |                      |
| alpha            | 7080                  |                       | 9080                 |                       | 8080                 |
| ratel            |                       |                       |                      |                       | 8000                 |

[^1]: Dgraph Zero uses port 5080 for internal communication within the cluster, and to support the fast data loading tools: Dgraph Live Loader and Dgraph Bulk Loader.
[^2]: Dgraph Zero uses port 6080 for administrative operations. Dgraph clients cannot access this port.

```shell
docker run --rm -it \
  -p 8088:8080 \
  -p 9088:9080 \
  -p 8009:8000 \
  -v .:/dgraph dgraph/standalone:v20.11.1
```

- `/graphql` is where you’ll find the GraphQL API for the types you’ve added. That is the single GraphQL entry point for your apps to Dgraph.
- `/admin` is where you’ll find an admin API for administering your GraphQL instance. That’s where you can update your GraphQL schema, perform health checks of your backend, and more.
	application/graphql
	application/json


# See Also
* [GraphQL](https://graphql.org)