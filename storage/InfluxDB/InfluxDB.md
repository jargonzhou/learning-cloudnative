# Notes of InfluxDB Codes

|时间|内容|
|:---|:---|
|20210812|kick off: version OSS 2.0|

## 术语

<!-- 记录阅读过程中出现的关键字及其简单的解释. -->

## 介绍

<!-- 描述软件的来源、特性、解决的关键性问题等. -->

## 动机

<!-- 描述阅读软件源码的动机, 要达到什么目的等. -->

## 系统结构

<!-- 描述软件的系统结构, 核心和辅助组件的结构; 系统较复杂时细分展示. -->

## 使用

<!-- 记录软件如何使用. -->

```
docker run --name influxdb -p 8086:8086 --volume .:/var/lib/influxdb influxdb:2.0.7
```

## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

- [InfluxDB key concepts](https://docs.influxdata.com/influxdb/v2.0/reference/key-concepts/): data elements, tabular data schema

> InfluxDB data elements are stored in **time-structured merge tree (TSM)** and **time series index (TSI)** files to efficiently compact stored data.

- [InfluxDB syntaxes](https://docs.influxdata.com/influxdb/v2.0/reference/syntax/): Flux syntax ( [Flux standard library](https://docs.influxdata.com/influxdb/v2.0/reference/flux/stdlib/), [Flux language specification](https://docs.influxdata.com/influxdb/v2.0/reference/flux/language/) ), Line protocol, Annotated CSV, Delete predicate syntax

- [Query data with InfluxQL](https://docs.influxdata.com/influxdb/v2.0/query-data/influxql/)

> In InfluxDB 1.x, data is stored in databases and retention policies. In InfluxDB OSS 2.0, data is stored in buckets. Because InfluxQL uses the 1.x data model, a bucket must be mapped to a database and retention policy (DBRP) before it can be queried using InfluxQL.

- [InfluxDB internals](https://docs.influxdata.com/influxdb/v2.0/reference/internals/): storage engine, file system layout, shards and shard groups, system buckets

## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->

## 其他备注
