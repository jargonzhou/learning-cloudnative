//
// LOAD CSV
// application-store/data-engineering/neo4j/import/building-knowledge-graphs
//
// Place
LOAD CSV WITH HEADERS FROM 'file:///var/lib/neo4j/import/Place.csv' AS line
MERGE (n:Place {country: line.country, city: line.city})
RETURN n;

// Person
LOAD CSV WITH HEADERS FROM 'file:///var/lib/neo4j/import/Person.csv' AS line
MERGE (p:Person {name: line.name})
SET p.age = line.age
SET p.gender = line.gender;

// FRIEND
LOAD CSV WITH HEADERS FROM "file:///var/lib/neo4j/import/FRIEND.csv" AS line
MATCH (p1:Person {name: line.from}), (p2:Person {name: line.to})
MERGE (p1)-[:FRIEND]->(p2);

// LIVES_IN
LOAD CSV WITH HEADERS FROM "file:///var/lib/neo4j/import/LIVES_IN.csv" AS line
MATCH (person:Person {name: line.from}), (place:Place {city: line.to})
MERGE (person)-[r:LIVES_IN]->(place)
SET r.since = line.since;