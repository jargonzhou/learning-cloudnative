# TiDB
* https://github.com/pingcap/tidb

License: Apache 2.0, Language: Go

TiDB (/’taɪdiːbi:/, "Ti" stands for Titanium) is an open-source, cloud-native, distributed SQL database designed for high availability, horizontal and vertical scalability, strong consistency, and high performance.

Key Features
- **Distributed Transactions/分布式事务**: TiDB uses a two-phase commit protocol to ensure ACID compliance, providing strong consistency. Transactions span multiple nodes, and TiDB's distributed nature ensures data correctness even in the presence of network partitions or node failures.
- **Horizontal and Vertical Scalability/水平和垂直扩展**: TiDB can be scaled horizontally by adding more nodes or vertically by increasing resources of existing nodes, all without downtime. TiDB's architecture separates computing from storage, enabling you to adjust both independently as needed for flexibility and growth.
- **High Availability/高可用**: Built-in Raft consensus protocol ensures reliability and automated failover. Data is stored in multiple replicas, and transactions are committed only after writing to the majority of replicas, guaranteeing strong consistency and availability, even if some replicas fail. Geographic placement of replicas can be configured for different disaster tolerance levels.
- **Hybrid Transactional/Analytical Processing (HTAP)/混合事务/分析处理**: TiDB provides two storage engines: TiKV, a row-based storage engine, and TiFlash, a columnar storage engine. TiFlash uses the Multi-Raft Learner protocol to replicate data from TiKV in real time, ensuring consistent data between the TiKV row-based storage engine and the TiFlash columnar storage engine. The TiDB Server coordinates query execution across both TiKV and TiFlash to optimize performance.
- **Cloud-Native/云原生**: TiDB can be deployed in public clouds, on-premises, or natively in Kubernetes. TiDB Operator helps manage TiDB on Kubernetes, automating cluster operations, while TiDB Cloud provides a fully-managed service for easy and economical deployment, allowing users to set up clusters with just a few clicks.
- **MySQL Compatibility/MySQL兼容性**: TiDB is compatible with MySQL 8.0, allowing you to use familiar protocols, frameworks and tools. You can migrate applications to TiDB without changing any code, or with minimal modifications. Additionally, TiDB provides a suite of data migration tools to help easily migrate application data into TiDB.
- **Open Source Commitment/开源承诺**: Open source is at the core of TiDB's identity. All source code is available on GitHub under the Apache 2.0 license, including enterprise-grade features. TiDB is built with the belief that open source enables transparency, innovation, and collaboration. We actively encourage contributions from the community to help build a vibrant and inclusive ecosystem, reaffirming our commitment to open development and accessibility for everyone.
