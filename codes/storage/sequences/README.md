
Sequences
---------

# API

[ISequencer](sequences-api/src/main/java/com/spike/giantdataanalysis/sequences/api/ISequencer.java)

	  long START_VALUE = 1L;

	  void init(String name, long value);

	  Sequence current(String name);

	  Sequence next(String name);

	  SequenceGroup next(String name, int n);


# Built on existing databases

+ MySQL: [MySQLSequencer](sequences-api-mysql/src/main/java/com/spike/giantdataanalysis/sequences/api/mysql/MySQLSequencer.java)

+ HBase: [HBaseSequencer](sequences-api-hbase/src/main/java/com/spike/giantdataanalysis/sequences/api/hbase/HBaseSequencer.java)

+ Cassandra: [CassandraSequencer](sequences-api-cassandra/src/main/java/com/spike/giantdataanalysis/sequences/api/cassandra/CassandraSequencer.java)
