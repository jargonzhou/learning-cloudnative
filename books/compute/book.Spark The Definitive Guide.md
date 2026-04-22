# Spark: The Definitive Guide

Version in book: 2.2

- I. Gentle Overview of Big Data and Spark: 1-4
- II. Structured APIs—DataFrames, SQL, and Datasets: 4-11
- III. Low-Level APIs: 12-14
- IV. Production Applications: 15-19
- V. Streaming: 20-23
- VI. Advanced Analytics and Machine Learning: 24-31
- VII. Ecosystem: 32-33

# 1 What Is Apache Spark?
- Apache Spark’s Philosophy
- Context: The Big Data Problem
- History of Spark
- The Present and Future of Spark
- Running Spark
# 2 A Gentle Introduction to Spark
- Spark’s Basic Architecture
- Spark’s Language APIs
- Spark’s APIs
- Starting Spark
- The SparkSession

The `SparkSession` instance is the way Spark executes user-defiined manipulations across the cluster.
There is a one-to-one correspondence between a `SparkSession` and a Spark Application.

In Scala, the variable is available as `spark` when we start the console.

> [!note] `SparkSession`
```scala
    package org.apache.spark.sql
    
    class SparkSession extends Serializable with Closeable with Logging
    object SparkSession extends Logging with Serializable
```

- DataFrames
- Transformations
- Actions
- Spark UI
- An End-to-End Example

# 3 A Tour of Spark’s Toolset

> [!note] "Tour"

- Running production applications with spark-submit
- Datasets: type-safe APIs for structured data
- Structured Streaming
- Machine learning and advanced analytics
- Resilient Distributed Datasets (RDD): Spark’s low level APIs
- SparkR
- The third-party package ecosystem


- Running Production Applications

`spark-submit` does one thing: it lets you send your application code to a cluster and launch it to execute there. 
Upon submission, the application will run until it exits
(completes the task) or encounters an error. 
You can do this with all of Spark’s support cluster managers including *Standalone*, *Mesos*, and *YARN*.

> [!example] "Run an application on local machine"

```shell
./bin/spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master local \
  ./examples/jars/spark-examples_2.11-2.2.0.jar 10
```

- Datasets: Type-Safe Structured APIs

**Datasets** a type-safe version of Spark’s structured API, for writing statically typed code in Java and Scala. 

The Dataset API gives users the ability to assign a Java/Scala class to the records within a `DataFrame` and manipulate it as a collection of typed objects, similar to a Java `ArrayList` or Scala `Seq`. 

The `Dataset` class is parameterized with the type of object contained inside: `Dataset<T>` in Java and `Dataset[T]` in Scala.
As of Spark 2.0, the supported types are classes following the *JavaBean pattern* in Java and *case classes* in Scala.

> [!example] "`Dataset`"

```scala
case class Flight(DEST_COUNTRY_NAME: String,
  ORIGIN_COUNTRY_NAME: String,
  count: BigInt)

val flightsDF = spark.read
  .parquet("/data/flight-data/parquet/2010-summary.parquet/")
val flights = flightsDF.as[Flight]

// collect objects of the proper type
flights
  .filter(flight_row => flight_row.ORIGIN_COUNTRY_NAME != "Canada")
  .map(flight_row => flight_row)
  .take(5)

flights
  .take(5)
  .filter(flight_row => flight_row.ORIGIN_COUNTRY_NAME != "Canada")
  .map(fr => Flight(fr.DEST_COUNTRY_NAME, fr.ORIGIN_COUNTRY_NAME, fr.count + 5))
```

> We cover Datasets in depth in Chapter 11.

- Structured Streaming

**Structured Streaming** is a high-level API for stream processing that became production-ready in Spark 2.2. 
With Structured Streaming, you can take the same operations that you perform in batch mode using Spark’s structured APIs and run them in a streaming fashion.

> [!example] "Structured Streaming"

Use `DataFrame` to analyze a static dataset:

```scala
val staticDataFrame = spark.read.format("csv")
  .option("header", "true")
  .option("inferSchema", "true")
  .load("/data/retail-data/by-day/*.csv")

staticDataFrame.createOrReplaceTempView("retail_data")
val staticSchema = staticDataFrame.schema

// group and aggregate the data
import org.apache.spark.sql.functions.{window, column, desc, col}
staticDataFrame
  .selectExpr(
    "CustomerId",
    "(UnitPrice * Quantity) as total_cost",
    "InvoiceDate")
  .groupBy(
    col("CustomerId"), window(col("InvoiceDate"), "1 day"))
  .sum("total_cost")
  .show(5)

+----------+--------------------+------------------+
|CustomerId| window| sum(total_cost)|
+----------+--------------------+------------------+
| 17450.0  |[2011-09-20 00:00...| 71601.44         |
...
| null     |[2011-12-08 00:00...|31975.590000000007|
+----------+--------------------+------------------+  
```

Use structured streaming:

```scala
val streamingDataFrame = spark.readStream
  .schema(staticSchema)
  .option("maxFilesPerTrigger", 1)
  .format("csv")
  .option("header", "true")
  .load("/data/retail-data/by-day/*.csv")

streamingDataFrame.isStreaming // returns true

// summation
val purchaseByCustomerPerHour = streamingDataFrame
  .selectExpr(
    "CustomerId",
    "(UnitPrice * Quantity) as total_cost",
    "InvoiceDate")
  .groupBy(
    $"CustomerId", window($"InvoiceDate", "1 day"))
  .sum("total_cost")

// call streaming action
purchaseByCustomerPerHour.writeStream
  .format("memory") // memory = store in-memory table
  .queryName("customer_purchases") // the name of the in-memory table
  .outputMode("complete") // complete = all the counts should be in the table
  .start()

// call streaming action to write to console
purchaseByCustomerPerHour.writeStream
  .format("console")
  .queryName("customer_purchases_2")
  .outputMode("complete")
  .start()

// run queries
spark.sql("""
  SELECT *
  FROM customer_purchases
  ORDER BY `sum(total_cost)` DESC
  """)
  .show(5)
```

- Machine Learning and Advanced Analytics

Another popular aspect of Spark is its ability to perform large-scale machine learning with a built-in library of machine learning algorithms called **MLlib**. 
MLlib allows for preprocessing, munging, training of models, and making predictions at scale on data. 

Machine learning algorithms in MLlib require that data is represented as *numerical values*.

> [!example] "MLlib"

Data:

```scala
staticDataFrame.printSchema()

root
|-- InvoiceNo: string (nullable = true)
|-- StockCode: string (nullable = true)
|-- Description: string (nullable = true)
|-- Quantity: integer (nullable = true)
|-- InvoiceDate: timestamp (nullable = true)
|-- UnitPrice: double (nullable = true)
|-- CustomerID: double (nullable = true)
|-- Country: string (nullable = true)
```

Transform data into numerical representation:

```scala
import org.apache.spark.sql.functions.date_format
val preppedDataFrame = staticDataFrame
  .na.fill(0)
  .withColumn("day_of_week", date_format($"InvoiceDate", "EEEE"))
  .coalesce(5)
```

Split data into training and test sets:

```scala
val trainDataFrame = preppedDataFrame
  .where("InvoiceDate < '2011-07-01'")
val testDataFrame = preppedDataFrame
  .where("InvoiceDate >= '2011-07-01'")

trainDataFrame.count()
testDataFrame.count()
```

Use MLlib's transformations:

```scala
import org.apache.spark.ml.feature.StringIndexer
val indexer = new StringIndexer()
  .setInputCol("day_of_week")
  .setOutputCol("day_of_week_index")

import org.apache.spark.ml.feature.OneHotEncoder
val encoder = new OneHotEncoder()
  .setInputCol("day_of_week_index")
  .setOutputCol("day_of_week_encoded")

import org.apache.spark.ml.feature.VectorAssembler
val vectorAssembler = new VectorAssembler()
  .setInputCols(Array("UnitPrice", "Quantity", "day_of_week_encoded"))
  .setOutputCol("features")  
```

Set up pipeline:

```scala
import org.apache.spark.ml.Pipeline
val transformationPipeline = new Pipeline()
  .setStages(Array(indexer, encoder, vectorAssembler))
```

Traning: (1) fit transformers to the dataset (2) use the pipeline to transform all the data

```scala
val fittedPipeline = transformationPipeline.fit(trainDataFrame)

val transformedTraining = fittedPipeline.transform(trainDataFrame)

// caching
transformedTraining.cache()
```

Train the model:

```scala
import org.apache.spark.ml.clustering.KMeans
val kmeans = new KMeans()
  .setK(20)
  .setSeed(1L)

val kmModel = kmeans.fit(transformedTraining)
```

Compute the cost:

```scala
kmModel.computeCost(transformedTraining)

val transformedTest = fittedPipeline.transform(testDataFrame)
kmModel.computeCost(transformedTest)
```

- Lower-Level APIs

Spark includes a number of lower-level primitives to allow for arbitrary Java and Python object manipulation via **Resilient Distributed Datasets (RDDs)**. 
RDDs are lower level than `DataFrame`s because they reveal physical execution characteristics (like partitions) to end users

```scala
// parallelize raw data that you have stored in memory
// on the driver machine
spark.sparkContext.parallelize(Seq(1, 2, 3)).toDF()
```

- SparkR

**SparkR** is a tool for running R on Spark. 

> [!example] SparkR
```R
library(SparkR)
sparkDF <- read.df("/data/flight-data/csv/2015-summary.csv",
  source = "csv", header="true", inferSchema = "true")
take(sparkDF, 5)
```

- Spark’s Ecosystem and Packages

One of the best parts about Spark is the ecosystem of packages and tools that the community has created. 

The index of Spark packages: `sparkpackages.org`.
# 4 Structured API Overview

- DataFrames and Datasets

> [!note] `DataFrame`, `Dataset`

```scala
package org.apache.spark.sql

class Dataset[T] extends Serializable

trait Row extends Serializable
object Row
```

```scala
package org.apache.spark

package object sql {
    type DataFrame = Dataset[Row]
}
```

- Schemas

> [!note] Spark Scala Type Reference

```scala
// import org.spache.spark.sql.types._

package org.apache.spark
package sql
package types

AnyDataType
ArrayType
AtomicType
BinaryType
BooleanType
ByteType
CalendarIntervalType
CharType
DataType
DataTypes
DateType
DayTimeIntervalType
Decimal
DecimalType
DoubleType
FloatType
IntegerType
LongType
MapType
Metadata
MetadataBuilder
NullType
NumericType
ObjectType
SQLUserDefinedType
ShortType
StringType
StructField
StructType
TimestampType
UDTRegistration
UserDefinedType
VarcharType
YearMonthIntervalType
```

- Overview of Structured Spark Types
- Overview of Structured API Execution

# 5 Basic Structured Operations

- Schemas

```scala
org.apache.spark.sql.types.StructType
org.apache.spark.sql.types.StructField
```

- Columns and Expressions

> [!note] `Column`, `functions`

```scala
import org.apache.spark.sql.functions.{col, column, expr}

package org.apache.spark.sql

class Column extends Logging

object functions {
    // Returns a Column based on the given column name.
    def col(colName: String): Column
    
    // Returns a Column based on the given column name.
    def column(colName: String): Column
    
    // Parses the expression string into the column that it represents, similar to Dataset#selectExpr.
    def expr(expr: String): Column
}
```

- Records and Rows

> [!note] `Row`

```scala
package org.apache.spark.sql

trait Row extends Serializable
object Row extends Serializable
```

- DataFrame Transformations

> [!note] DataFrame Transformations

```scala
package org.apache.spark.sql

class SparkSession {
    // Creating DataFrames
    def createDataFrame(rowRDD: RDD[Row], schema: StructType): DataFrame
}

class Dataset[T] extends Serializable {
    // Converts this strongly typed collection of data to generic DataFrame with columns renamed.
    def toDF(colNames: String*): DataFrame

    // Selects a set of columns.
    def select(col: String, cols: String*): DataFrame

    // Selects a set of column based expressions.
    def select(cols: Column*): DataFrame

    // Selects a set of SQL expressions.
    def selectExpr(exprs: String*): DataFrame
    
    // Adding Columns
    // Returns a new Dataset by adding a column or replacing the existing column that has the same name.
    def withColumn(colName: String, col: Column): DataFrame
    
    // Renaming Columns
    // Returns a new Dataset with a column renamed.
    def withColumnRenamed(existingName: String, newName: String): DataFrame
    
    // Removing Columns
    // Returns a new Dataset with a column dropped.
    def drop(colName: String): DataFrame
    
    // Filtering Rows
    // (Scala-specific) Returns a new Dataset that only contains elements where func returns true.
    def filter(func: (T) ⇒ Boolean): Dataset[T]
    // Filters rows using the given SQL expression.
    def filter(conditionExpr: String): Dataset[T]
    // Filters rows using the given condition.
    def filter(condition: Column): Dataset[T]
    // Filters rows using the given SQL expression.
    def where(conditionExpr: String): Dataset[T]
    // Filters rows using the given condition.
    def where(condition: Column): Dataset[T]
    
    // Getting Uniqu Rows
    // Returns a new Dataset that contains only the unique rows from this Dataset.
    def distinct(): Dataset[T]
    
    // Random Samples
    // Returns a new Dataset by sampling a fraction of rows, using a user-supplied seed.
    def sample(withReplacement: Boolean, fraction: Double, seed: Long): Dataset[T]
    
    // Random Splits
    // Randomly splits this Dataset with the provided weights.
    def randomSplit(weights: Array[Double], seed: Long): Array[Dataset[T]]
    
    // Union: Concatenating and Appending Rows
    // Returns a new Dataset containing union of rows in this Dataset and another Dataset.
    def union(other: Dataset[T]): Dataset[T]
    
    // Sorting Rows
    // Returns a new Dataset sorted by the given expressions.
    def sort(sortExprs: Column*): Dataset[T]
    // Returns a new Dataset sorted by the specified column, all in ascending order.
    def sort(sortCol: String, sortCols: String*): Dataset[T]
    // Returns a new Dataset with each partition sorted by the given expressions.
    def sortWithinPartitions(sortExprs: Column*): Dataset[T]
    // Returns a new Dataset with each partition sorted by the given expressions.
    def sortWithinPartitions(sortCol: String, sortCols: String*): Dataset[T]
    // Returns a new Dataset sorted by the given expressions.
    def orderBy(sortExprs: Column*): Dataset[T]
    // Returns a new Dataset sorted by the given expressions.
    def orderBy(sortCol: String, sortCols: String*): Dataset[T]
    
    // Limit
    // Returns a new Dataset by taking the first n rows.
    def limit(n: Int): Dataset[T]
    
    // Repartition and Coalesce
    // Returns a new Dataset partitioned by the given partitioning expressions into numPartitions.
    def repartition(numPartitions: Int, partitionExprs: Column*): Dataset[T]
    // Returns a new Dataset that has exactly numPartitions partitions, when the fewer partitions are requested.
    def coalesce(numPartitions: Int): Dataset[T]
    
    // Collecting Rows to the Driver
    // Returns an array that contains all rows in this Dataset.
    def collect(): Array[T]
    // Returns the first n rows in the Dataset.
    def take(n: Int): Array[T]
    // Displays the Dataset in a tabular form.
    def show(numRows: Int, truncate: Boolean): Unit
    // Returns an iterator that contains all rows in this Dataset.
    def toLocalIterator(): Iterator[T]
}

object functions {
    // Creates a Column of literal value.
    def lit(literal: Any): Column
    
    // Sorting
    // Returns a sort expression based on ascending order of the column.
    def asc(columnName: String): Column
    // Returns a sort expression based on the descending order of the column.
    def desc(columnName: String): Column
}
```


# 6 Working with Different Types of Data

- Where to Look for APIs

```scala
DataFrame
Dataset
DataFrameStatFunctions // Statistic functions for DataFrames.
DataFrameNaFunctions   // Functionality for working with missing data in DataFrames.
Column
org.apache.spark.sql.functions
```

- Converting to Spark Types

```scala
lit
```

- Working with Booleans

```scala
equalTo
===
=!=
not

contains
isin
or
and

eqNullSafe
```

- Working with Numbers

```scala
pow
round
bround
corr

count
mean
stddev_pop
min
max

monotonically_increasing_id
rand
randn
```

- Working with Strings

```scala
initcap
lower
upper
lpad
ltrim
rpad
rtrim
trim

regexp_extract
regexp_replace
translate
```

- Working with Dates and Timestamps

```scala
current_date
current_timestamp
date_sub
date_add
datediff
months_between
to_date
to_timestamp
```

- Working with Nulls in Data

```scala
coalesce
ifnull
nullif
nvl
nvl2

// df.na
drop
fill
replace
```

- Ordering

```scala
asc_nulls_first
desc_nulls_first
asc_nulls_last
desc_nulls_last
```

- Working with Complex Types

```scala
struct

split
size
array_contains
explode

map
```

- Working with JSON

```scala
get_json_object
json_tuple
to_json
from_json
```

- User-Defined Functions

```scala
udf

UDFRegistration.register
SparkSession.builder().enableHiveSupport()
```


# 7 Aggregations


```scala
package org.apache.spark.sql

// A set of methods for aggregations on a DataFrame, created by groupBy, cube or rollup (and also pivot).
// The main method is the agg function, which has multiple variants. 
// This class also contains some first-order statistics such as mean, sum for convenience.
class RelationalGroupedDataset extends AnyRef
```

- Aggregation Functions

```scala
org.apache.spark.sql.functions

package org.apache.spark.sql
class Dataset[T] extends Serializable {
    // Returns a DataFrameStatFunctions for working statistic functions support.
    def stat: DataFrameStatFunctions
}
```

```scala
count
countDistinct
approx_count_distinct
first
last
min
max
sum
sumDistinct
avg
mean

// Variance and Standard Deviation
var_pop
stddev_pop
var_samp
stddev_samp
variance
stddev

// skewness and kurtosis
skewness
kurtosis

// Covariance and Correlation
corr
covar_pop
covar_samp

// Aggregating to Complex Types
collect_set
collect_list
```

- Grouping

```scala
df.groupBy(...).count()
org.apache.spark.sql.functions.count
df.groupBy(...).agg(...)
```

- Window Functions
- Grouping Sets
- User-Defined Aggregation Functions


# 8 Joins

-  5.8.1 Join Expressions
-  5.8.2 Join Types
-  5.8.3 Inner Joins
-  5.8.4 Outer Joins
-  5.8.5 Left Outer Joins
-  5.8.6 Right Outer Joins
-  5.8.7 Left Semi Joins
-  5.8.8 Left Anti Joins
-  5.8.9 Natural Joins
-  5.8.10 Cross (Cartesian) Joins
-  5.8.11 Challenges When Using Joins
-  5.8.12 How Spark Performs Joins

# 9 Data Sources

-  5.9.1 The Structure of the Data Sources API
-  5.9.2 CSV Files
-  5.9.3 JSON Files
-  5.9.4 Parquet Files
-  5.9.5 ORC Files
-  5.9.6 SQL Databases
-  5.9.7 Text Files
-  5.9.8 Advanced I/O Concepts

# 10 Spark SQL

-  5.10.1 What Is SQL?

Spark implements a subset of ANSI SQL:2003.

-  5.10.2 Big Data and SQL: Apache Hive
-  5.10.3 Big Data and SQL: Spark SQL
-  5.10.4 How to Run Spark SQL Queries

```shell
./bin/spark-sql

spark.sql("SELECT 1 + 1").show()

./sbin/start-thriftserver.sh
./bin/beeline
```

-  5.10.5 Catalog

```scala
package org.apache.spark.sql.catalog

abstract class Catalog
```

-  5.10.6 Tables

```scala
df.saveAsTable
```

-  5.10.7 Views
-  5.10.8 Databases
-  5.10.9 Select Statements
-  5.10.10 Advanced Topics
-  5.10.11 Miscellaneous Features

# 11 Datasets

-  5.11.1 When to Use Datasets
-  5.11.2 Creating Datasets
-  5.11.3 Actions
-  5.11.4 Transformations
-  5.11.5 Joins
-  5.11.6 Grouping and Aggregations
# 12 Resilient Distributed Datasets (RDDs)

-  5.13.1 What Are the Low-Level APIs?
-  5.13.2 About RDDs
-  5.13.3 Creating RDDs
-  5.13.4 Manipulating RDDs
-  5.13.5 Transformations
-  5.13.6 Actions
-  5.13.7 Saving Files
-  5.13.8 Caching
-  5.13.9 Checkpointing
-  5.13.10 Pipe RDDs to System Commands

# 13 Advanced RDDs

-  5.14.1 Key-Value Basics (Key-Value RDDs)
-  5.14.2 Aggregations
-  5.14.3 CoGroups
-  5.14.4 Joins
-  5.14.5 Controlling Partitions
-  5.14.6 Custom Serialization

# 14 Distributed Shared Variables

-  5.15.1 Broadcast Variables
-  5.15.2 Accumulators
# 15 How Spark Runs on a Cluster

-  5.17.1 The Architecture of a Spark Application
-  5.17.2 The Life Cycle of a Spark Application (Outside Spark)
-  5.17.3 The Life Cycle of a Spark Application (Inside Spark)
-  5.17.4 Execution Details

# 16 Developing Spark Applications

-  5.18.1 Writing Spark Applications
-  5.18.2 Testing Spark Applications
-  5.18.3 The Development Process
-  5.18.4 Launching Applications
-  5.18.5 Configuring Applications

# 17 Deploying Spark

-  5.19.1 Where to Deploy Your Cluster to Run Spark Applications
-  5.19.2 Cluster Managers
-  5.19.3 Miscellaneous Considerations

# 18 Monitoring and Debugging

-  5.20.1 The Monitoring Landscape
-  5.20.2 What to Monitor
-  5.20.3 Spark Logs
-  5.20.4 The Spark UI
-  5.20.5 Debugging and Spark First Aid

# 19 Performance Tuning

-  5.21.1 Indirect Performance Enhancements
-  5.21.2 Direct Performance Enhancements

# 20 Stream Processing Fundamentals

-  5.23.1 What Is Stream Processing?
-  5.23.2 Stream Processing Design Points
-  5.23.3 Spark’s Streaming APIs

# 21 Structured Streaming Basics

-  5.24.1 Structured Streaming Basics
-  5.24.2 Core Concepts
-  5.24.3 Structured Streaming in Action
-  5.24.4 Transformations on Streams
-  5.24.5 Input and Output
-  5.24.6 Streaming Dataset API

# 22 Event-Time and Stateful Processing

-  5.25.1 Event Time
-  5.25.2 Stateful Processing
-  5.25.3 Arbitrary Stateful Processing
-  5.25.4 Event-Time Basics
-  5.25.5 Windows on Event Time
-  5.25.6 Dropping Duplicates in a Stream
-  5.25.7 Arbitrary Stateful Processing

# 23 Structured Streaming in Production

-  5.26.1 Fault Tolerance and Checkpointing
-  5.26.2 Updating Your Application
-  5.26.3 Metrics and Monitoring
-  5.26.4 Alerting
-  5.26.5 Advanced Monitoring with the Streaming Listener
# 24 Advanced Analytics and Machine Learning Overview

-  5.28.1 A Short Primer on Advanced Analytics
-  5.28.2 Spark’s Advanced Analytics Toolkit
-  5.28.3 High-Level MLlib Concepts
-  5.28.4 MLlib in Action
-  5.28.5 Deployment Patterns

# 25 Preprocessing and Feature Engineering

-  5.29.1 Formatting Models According to Your Use Case
-  5.29.2 Transformers
-  5.29.3 Estimators for Preprocessing
-  5.29.4 High-Level Transformers
-  5.29.5 Working with Continuous Features
-  5.29.6 Working with Categorical Features
-  5.29.7 Text Data Transformers
-  5.29.8 Feature Manipulation
-  5.29.9 Feature Selection
-  5.29.10 Advanced Topics
-  5.29.11 Writing a Custom Transformer

# 26 Classification

-  5.30.1 Use Cases
-  5.30.2 Types of Classification
-  5.30.3 Classification Models in MLlib
-  5.30.4 Logistic Regression
-  5.30.5 Decision Trees
-  5.30.6 Random Forest and Gradient-Boosted Trees
-  5.30.7 Naive Bayes
-  5.30.8 Evaluators for Classification and Automating Model Tuning
-  5.30.9 Detailed Evaluation Metrics
-  5.30.10 One-vs-Rest Classifier
-  5.30.11 Multilayer Perceptron

# 27 Regression

-  5.31.1 Use Cases
-  5.31.2 Regression Models in MLlib
-  5.31.3 Linear Regression
-  5.31.4 Generalized Linear Regression
-  5.31.5 Decision Trees
-  5.31.6 Random Forests and Gradient-Boosted Trees
-  5.31.7 Advanced Methods
-  5.31.8 Evaluators and Automating Model Tuning
-  5.31.9 Metrics

# 28 Recommendation

-  5.32.1 Use Cases
-  5.32.2 Collaborative Filtering with Alternating Least Squares
-  5.32.3 Evaluators for Recommendation
-  5.32.4 Metrics
-  5.32.5 Frequent Pattern Mining

# 29 Unsupervised Learning

-  5.33.1 Use Cases
-  5.33.2 Model Scalability
-  5.33.3 k-means
-  5.33.4 Bisecting k-means
-  5.33.5 Gaussian Mixture Models
-  5.33.6 Latent Dirichlet Allocation

# 30 Graph Analytics

-  5.34.1 Building a Graph
-  5.34.2 Querying the Graph
-  5.34.3 Motif Finding
-  5.34.4 Graph Algorithms
# 31 Deep Learning

-  5.35.1 What Is Deep Learning?
-  5.35.2 Ways of Using Deep Learning in Spark
-  5.35.3 Deep Learning Libraries
-  5.35.4 A Simple Example with Deep Learning Pipelines


# 32 Language Specifics: Python (PySpark) and R (SparkR and sparklyr)

-  5.37.1 PySpark
-  5.37.2 R on Spark

# 33 Ecosystem and Community

-  5.38.1 Spark Packages
-  5.38.2 Community
