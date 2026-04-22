// https://neo4j.com/docs/graph-data-science/current/management-ops/graph-creation/graph-project-cypher-projection/
MATCH (p1:Person)-[:FRIEND]->(p2:Person)
WITH gds.graph.project('gds-example-graph', p1, p2) AS g
RETURN g.graphName AS graph, g.nodeCount AS nodes, g.relationshipCount AS rels;

CALL
  gds.betweenness.write(
    'gds-example-graph',
    {writeProperty: 'betweennessCentrality'}
  );

CALL gds.graph.drop('gds-example-graph') YIELD graphName;

MATCH (n)
RETURN n;