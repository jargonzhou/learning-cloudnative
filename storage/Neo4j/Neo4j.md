# Neo4j
* https://github.com/neo4j/neo4j
* https://neo4j.com/

actions
- [Building Knowledge Graphs](../codes/neo4j/building-knowledge-graphs/README.md)

Docs
- Neo4j DBMS
  - Getting Started
  - Operations
  - Migration and Upgrade
  - Status Codes
  - Java Reference
  - Kerberos Add-on
- Neo4j Aura
- Neo4j Tools
  - Neo4j Bloom
  - Neo4j Browser
  - Neo4j Data Importer
  - Neo4j Desktop
  - Neo4j Ops Manager
  - Neodash commercial
- Neo4j Graph Data Science
  - Neo4j Graph Data Science Library
  - Neo4j Graph Data Science Client
- Cypher Query Language
  - Cypher
  - Cypher Cheat Sheet
  - APOC Library
- Generative AI
  - Neo4j GraphRAG for Python
  - Embeddings and vector indexes tutorial
  - GenAI integrations
  - Vector search indexes
  - Vector search functions
  - GraphQL vector index search documentation
- Create applications
  - Python Driver
  - Go Driver
  - Java Driver
  - JDBC Driver
  - JavaScript Driver
  - .Net Driver
  - Neo4j GraphQL Library
  - Neo4j Visualization Library
  - OGM Library
  - Spring Data Neo4j
  - HTTP API Neo4j Query API
  - Bolt
- Connect data sources
  - Neo4j Connector for Apache Spark
  - Neo4j Connector for Apache Kafka
  - Change Data Capture (CDC)
  - BigQuery to Neo4j
  - Google Cloud to Neo4j

# DBMS
- [Getting Started](https://neo4j.com/docs/getting-started/current/)
- [Operations](https://neo4j.com/docs/operations-manual/current/)
- [Migration and Upgrade](https://neo4j.com/docs/migration-guide/current/)
- [Status Codes](https://neo4j.com/docs/status-codes/current/)
- Java Reference: The Java Reference contains information on advanced Java-centric usage of Neo4j.
- [Kerberos Add-on](https://neo4j.com/docs/kerberos-add-on/current/)

## Java Reference
* https://neo4j.com/docs/java-reference/current/

The Java Reference contains information on advanced Java-centric usage of Neo4j.

It covers the following topics:
- **Extending Neo4j/扩展Neo4j** — How to build unmanaged extensions and procedures/扩展和过程.
- **Using Neo4j embedded in Java applications/嵌入在Java应用中** — Instructions on embedding Neo4j in .
- **Traversal Framework/遍历框架** — A walkthrough of the traversal framework.
- **Transaction management/事务管理** — Examples on transaction management in Neo4j.
- **JMX metrics/JMX度量** — How to monitor Neo4j with JMX and a reference of available metrics.

# Deployment

# Cypher Query Language

## Example: Movie Graph

Create the data
```cypher
CREATE CONSTRAINT movie_title IF NOT EXISTS FOR (m:Movie) REQUIRE m.title IS UNIQUE;
CREATE CONSTRAINT person_name IF NOT EXISTS FOR (p:Person) REQUIRE p.name IS UNIQUE;

MERGE (TheMatrix:Movie {title:'The Matrix'}) ON CREATE SET TheMatrix.released=1999, TheMatrix.tagline='Welcome to the Real World'

MERGE (Keanu:Person {name:'Keanu Reeves'}) ON CREATE SET Keanu.born=1964
MERGE (Carrie:Person {name:'Carrie-Anne Moss'}) ON CREATE SET Carrie.born=1967
MERGE (Laurence:Person {name:'Laurence Fishburne'}) ON CREATE SET Laurence.born=1961
MERGE (Hugo:Person {name:'Hugo Weaving'}) ON CREATE SET Hugo.born=1960
MERGE (LillyW:Person {name:'Lilly Wachowski'}) ON CREATE SET LillyW.born=1967
MERGE (LanaW:Person {name:'Lana Wachowski'}) ON CREATE SET LanaW.born=1965
MERGE (JoelS:Person {name:'Joel Silver'}) ON CREATE SET JoelS.born=1952

MERGE (Keanu)-[:ACTED_IN {roles:['Neo']}]->(TheMatrix)
MERGE (Carrie)-[:ACTED_IN {roles:['Trinity']}]->(TheMatrix)
MERGE (Laurence)-[:ACTED_IN {roles:['Morpheus']}]->(TheMatrix)
MERGE (Hugo)-[:ACTED_IN {roles:['Agent Smith']}]->(TheMatrix)
MERGE (LillyW)-[:DIRECTED]->(TheMatrix)
MERGE (LanaW)-[:DIRECTED]->(TheMatrix)
MERGE (JoelS)-[:PRODUCED]->(TheMatrix)


MATCH (person:Person {name: 'Tom Hanks'})
MATCH path = (person)-[:ACTED_IN]->(m)<-[:DIRECTED]-(d)
RETURN path;
```

Indexes and constraints
```cypher
SHOW INDEXES

CREATE CONSTRAINT movie_title IF NOT EXISTS FOR (m:Movie) REQUIRE m.title IS UNIQUE;
CREATE CONSTRAINT person_name IF NOT EXISTS FOR (p:Person) REQUIRE p.name IS UNIQUE;

CREATE INDEX person_born IF NOT EXISTS FOR (p:Person) ON (p.born);
CREATE INDEX movie_released IF NOT EXISTS FOR (m:Movie) ON (m.released)
```

Find nodes
```
MATCH (p:Person {name: "Tom Hanks"})
RETURN p

MATCH (p:Person)
RETURN p.name LIMIT 10

MATCH (p:Person)
RETURN p LIMIT 10

MATCH (m:Movie) WHERE m.released >= 1990 AND m.released < 2000
RETURN m.title, m.released
```

Find patterns
```cypher
MATCH (p:Person {name: "Tom Hanks"})-[:ACTED_IN]->(m:Movie)
RETURN m.title

MATCH (:Person {name:"Tom Hanks"})-[:ACTED_IN]->(m)<-[:ACTED_IN]-(coActors)
RETURN coActors.name

MATCH path= (:Person {name:"Tom Hanks"})-[:ACTED_IN]->(m)<-[:ACTED_IN]-(coActors)
RETURN path

MATCH (p:Person)-[relationship]-(:Movie {title: "Cloud Atlas"})
RETURN p.name, type(relationship), relationship
```

Six degrees of Kevin Bacon
```cypher
MATCH (:Person {name:"Kevin Bacon"})-[*1..6]-(n)
RETURN DISTINCT n

MATCH path=shortestPath(
  (:Person {name:"Kevin Bacon"})-[*]-(:Person {name:"Meg Ryan"})
)
RETURN path, length(path) / 2 as distance
```

Recommendations
```cypher
// To find a list of potential co-actors, run the following query:
MATCH (p:Person {name:"Tom Hanks"})-[:ACTED_IN]->(m)<-[:ACTED_IN]-(coActors),
  (coActors)-[:ACTED_IN]->(m2)<-[:ACTED_IN]-(cocoActors)
WHERE NOT exists { (p)-[:ACTED_IN]->()<-[:ACTED_IN]-(cocoActors) } AND p <> cocoActors
RETURN cocoActors.name AS recommended, count(*) AS score ORDER BY score DESC

// if you think it would be a good idea for Tom Hanks and Keanu Reeves to do a movie together, who would be able to introduce them, and in which movies have they acted jointly?
MATCH (p1:Person {name:"Tom Hanks"})-[:ACTED_IN]->(m)<-[:ACTED_IN]-(coActors),
  (coActors)-[:ACTED_IN]->(m2)<-[:ACTED_IN]-(p2:Person {name:"Keanu Reeves"})
RETURN DISTINCT coActors.name AS matchmaker
```

Clean up
```cypher
MATCH (n:Person|Movie)
DETACH DELETE n

MATCH ()
RETURN count (*)
```

# Tools
- **Neo4j Bloom/Explore**: Visualize and explore graph data using natural language search and an intuitive and graphical interface.
- **Neo4j Browser/Query**: Write and execute Cypher® queries and visualize the results in nodes and relationships.
- **Neo4j Data Importer**: Learn how to model and import data to your Neo4j database.
  - [Other ways of importing data into Neo4j](https://neo4j.com/docs/data-importer/current/import-others/): Cypher `LOAD CSV`, Neo4j-admin, Neo4j connectors, APOC Load procedures
- **Neo4j Desktop**: Learn how to experience Neo4j on your local desktop.
- **Neo4j Ops Manager**: Learn how to monitor, administer, and operate all of the Neo4j DBMSs in an Enterprise with Neo4j Ops Manager.

# Graph Data Science
* [Neo4j.Graph Data Science.md](./Neo4j.Graph%20Data%20Science.md)

# Create applications
- Python
- Go
- Java
- JDBC
- JavaScript
- .NET
- MCP
- GrapQL
- Object Graph Mapping Library
- Spring Data Neo4j
- API
  - Neo4j Query API: HTTP
  - Change Data Capture
  - Bolt: binary message protocol
  - Cypher Builder: JavaScript, TypeScript

# Connect data sources
- AWS Glue
- Apache Spark
- Apache Kafka
- Change Data Capture
- BI: Magnitude Simba for Tableau, PowerBI, ...
- Dataflow: BigQuery

# Neo4j Labs
* https://neo4j.com/labs/

Neo4j Labs is a collection of the latest innovations in graph technology. These projects are designed and developed by the Neo4j team as a way to test functionality and extensions of our product offerings. A project typically either graduates to being maintained as a formal Neo4j project or is deprecated with source made available publicly.

These Labs are supported via the online community. They are actively developed and maintained, but we don’t provide any SLAs or guarantees around backwards compatibility and deprecation, see the FAQ.

Current Projects
- GenAI Ecosystem
- MCP Servers
- Neo4j Agent Memory
- LLM Knowledge Graph Builder
- arrows.app
- Awesome Procedures on Cypher (APOC)
- ETL Tool
- Neo4j plugin for Liquibase
- Neo4j Aura Terraform Provider
- Neo4j-Migrations
- Neosemantics
- Rdflib-Neo4j
- NeoDash
- neomodel
- Needle Starter Kit
- Cypher Workbench
- Keymaker
- Spatial

Graduated Projects
- aura-cli
- Neo4j-Helm
- GraphQL and GRANDstack
- Graph Data Science Library
- Neo4j Connector for Apache Kafka
- Neo4j Connector for Apache Spark
- Neo4j Docker Container
- Halin Monitoring App

## APOC (Awesome Procedures on Cypher)
* [Core](https://neo4j.com/docs/apoc/5/)
  * https://github.com/neo4j/apoc
* [Extended](https://neo4j.com/labs/apoc/5/)
  * https://github.com/neo4j-contrib/neo4j-apoc-procedures

## Graph visualization tools
* https://neo4j.com/docs/getting-started/graph-visualization/graph-visualization-tools/

- 1. Standalone product tools
  - Neo4j Bloom
  - NeoDash
  - GraphXR
  - yFiles
  - Linkurious Enterprise
  - GraphAware Hume
  - Graphistry
  - Graphlytic
  - Perspectives
  - Keylines
  - Semspect
- 2. Embeddable tools with built-in Neo4j connections
  - Neovis.js
  - Popoto.js
- 3. Embeddable libraries without direct Neo4j connection
  - Neo4j Visualization Library (NVL)
  - D3.js
  - Vis.js
  - Sigma.js
  - Vivagraph.js
  - Cytoscape.js

# See Also
* [openCypher](https://opencypher.org/): openCypher is an open source specification of Cypher® - the most widely adopted query language for property graph databases. Cypher was developed by Neo4j®. Today, the specification of openCypher evolves towards ISO/IEC 39075 GQL — the property graph query language standard developed by ISO/IEC JTC1 SC32 WG3.