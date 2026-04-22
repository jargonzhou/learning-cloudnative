// Metadata KR
//
// Node
//
// DataSet {type, desc}
// Field {name, datatype}
// CatalogTerm {name, id, catalog}
// UserGroup {name}
// Domain {name}
// Task{name, type}
// Pipeline {name, type}
// User {name}
// DataPlatform {type}
// DataSink {type, desc}
//
// Edge
//
// Dataset -[hasField]-> Field
// Field -[associated_with]-> CatalogTerm
// CatalogTerm -[owned_by]-> UserGroup
// DataSet -[owned_by]-> User
// DataSet -[store]-> DataPlatform
// DataSet -[consumes]-> DataSink
// DataSet -[associated_with]-> Domain
// Task -[consumes]-> Dataset
// Task -[produces]-> Dataset
// Task -[part_of]-> Pipeline
MERGE (ds1:DataSet {type: 'CSV', desc: 'Sales by month'})
MERGE (ds2:DataSet {type: 'CSV', desc: 'Sales raw file'})
MERGE (ds3:DataSet {type: 'CSV', desc: 'Sales standardised file'})
MERGE (f1:Field {name: 'customerId', datatype: 'string'})
MERGE (f2:Field {name: 'address', datatype: 'string'})
MERGE (f3:Field {name: 'renewal_date', datatype: 'datetime'})

MERGE
  (ct1:CatalogTerm
    {
      name: 'location_info',
      id: 'http://schema.org/location',
      catalog: 'customer-1.4'
    })
MERGE (ug1:UserGroup {name: 'Catalog Team'})

MERGE (u1:User {name: 'John Doe'})

MERGE (d1:Domain {name: 'Corporate Sales'})

MERGE (t1:Task {name: 'standardise_reference_fields', type: 'record'})
MERGE (t2:Task {name: 'aggregate_by_month', type: 'record'})
MERGE (p1:Pipeline {name: '', type: ''})
MERGE (dp1:DataPlatform {name: 'HDFS file'})
MERGE (dsk1:DataSink {type: 'Dashboard', desc: 'monthly sales'})
MERGE (dsk2:DataSink {type: 'MLFeatureSet', desc: 'sales predictions'})
//
MERGE (ds1)-[:hasField]->(f1)
MERGE (ds1)-[:hasField]->(f2)
MERGE (f2)-[:associated_with]->(ct1)
MERGE (ct1)-[:owned_by]->(ug1)
MERGE (ds1)-[:hasField]->(f3)
MERGE (ds1)-[:owned_by]->(u1)
MERGE (ds1)-[:store]->(dp1)
MERGE (ds2)-[:store]->(dp1)
MERGE (ds3)-[:store]->(dp1)
MERGE (ds1)-[:consumes]->(dsk1)
MERGE (ds1)-[:consumes]->(dsk2)
MERGE (ds1)-[:associated_with]->(d1)

MERGE (t1)-[:consumes]->(ds2)
MERGE (t1)-[:part_of]->(p1)
MERGE (t1)-[:produces]->(ds3)
MERGE (t2)-[:part_of]->(p1)
MERGE (t2)-[:consumes]->(ds3)
MERGE (t2)-[:produces]->(ds1);

// Query all
MATCH
  (n:DataSet
    |Field
    |CatalogTerm
    |UserGroup
    |Domain
    |Task
    |Pipeline
    |User
    |DataPlatform
    |DataSink)-
    [p]->
  (n1)
RETURN n, p, n1;

// measures dataset popularity by the number of consumers using that data
MATCH (d:DataSet)
WHERE
  (d)-[:associated_with]->(:Domain {name: 'Corporate Sales'}) AND
  (d)-
  [:hasField]->
  (:Field)-
  [:associated_with]->
  (:CatalogTerm {name: 'location_info'})
RETURN
  d.id AS dataset_id,
  d.desc AS dataset_desc,
  d.type AS dataset_type,
  COUNT { (d)-[:consumes]->(dsk:DataSink) } AS dataset_usage_count;

// returns a list of data sinks and their owners
MATCH (t:Task)-[:produces|consumes*2..]-(:DataSet)-[:consumes]->(s:DataSink)
// -[:owned_by]->(o)
WHERE t.name = 'standardise_reference_fields'
RETURN
  s.id AS affectedDataConsumerID,
  s.type AS affectedDataConsumerType,
  s.desc AS affectedDataConsumerDesc;
// ,
// o.id AS ownerID,
// o.name AS ownerName

// A simple data lineage query
MATCH
  (s:DataSink)<-[:consumes]-
  (:DataSet)-[:produces|consumes*2..]-
  (raw:DataSet)-[:store]->
  (dp:DataPlatform)
WHERE s.type = 'Dashboard' // AND s.id = 'X'
RETURN
  raw.id AS sourceDatasetID,
  raw.type AS sourceDatasetType,
  raw.desc AS sourceDatasetDesc,
  dp.id AS sourcePlatformID,
  dp.name AS sourcePlatformName;