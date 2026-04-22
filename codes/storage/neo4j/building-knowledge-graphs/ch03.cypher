CREATE
  (:Person {name: 'Rosa'})-[:LIVES_IN {since: 2020}]->
  (:Place {city: 'Berlin', country: 'DE'});

// Node Key constraint requires Neo4j Enterprise Edition
// CREATE CONSTRAINT no_duplicate_cities
// FOR (p:Place)
// REQUIRE (p.country, p.city) IS NODE KEY;
MERGE (london:Place {city: 'London', country: 'UK'})
MERGE (rosa:Person {name: 'Rosa'})
MERGE (fred:Person {name: 'Fred'})
MERGE (fred)-[:LIVES_IN]->(london)
MERGE (karl:Person {name: 'Karl'})
MERGE (karl)-[:LIVES_IN]->(london)
MERGE (rosa)-[:FRIEND]->(karl)
MERGE (karl)-[:FRIEND]->(rosa)
MERGE (karl)-[:FRIEND]->(fred)
MERGE (fred)-[:FRIEND]->(karl);

MATCH (n)
RETURN (n);

MATCH (p:Person)
WHERE p.name = 'Rosa'
SET p.dob = 19841203;

MATCH (p:Person)
WHERE p.name = 'Rosa'
REMOVE p.dob;

// Who lives in Berlin?
MATCH (p:Person)-[:LIVES_IN]->(:Place {city: 'Berlin', country: 'DE'})
RETURN (p);

// Naive friends of friends
MATCH (rosa:Person {name: 'Rosa'})-[:FRIEND*2..2]->(fof:Person)
WHERE rosa <> fof
RETURN (fof);

// finding friends and friends of friends of someone who lives in Berlin
MATCH
  (:Place {city: 'Berlin'})<-[:LIVES_IN]-(p:Person)<-[:FRIEND*1..2]-(f:Person)
WHERE f <> p
RETURN f;

//
// Graph Global Queries
//
// Which are the most popular cities to live in?
MATCH (p:Place)<-[l:LIVES_IN]-(:Person)
RETURN p AS place, COUNT(l) AS rels
ORDER BY rels DESC;

//
// Calling Functions and Procedures
//
CALL db.schema.visualization();
// concate property string: Fred => Freddy
// There is no procedure with the name `apoc.atomic.concat` registered for this database instance. Please ensure you've spelled the procedure name correctly and that the procedure is properly deployed.
MATCH (p:Person {name: 'Fred'})
CALL apoc.atomic.concat(p, 'name', 'dy') YIELD oldValue, newValue
RETURN oldValue, newValue;

RETURN
  apoc.date.convert(datetime().epochSeconds, "seconds", "days") AS outputInDays;

//
// explain, profile
//
EXPLAIN
MATCH (n)
RETURN (n);

PROFILE
MATCH (n)
RETURN (n);

// clean up
MATCH (n)
DETACH DELETE n;