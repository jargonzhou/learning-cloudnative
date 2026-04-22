# Apache Airflow
* https://airflow.apache.org/

Apache Airflow® is a platform created by the community to programmatically author, schedule and monitor workflows.

# Artifacts
- **Apache Airflow®**: Apache Airflow Core, which includes webserver, scheduler, CLI and other components that are needed for minimal Airflow installation.
- **Apache Airflow CTL** (`airflowctl`): Apache Airflow CTL (airflowctl) is a command-line interface (CLI) for Apache Airflow that interacts exclusively with the Airflow REST API. It provides a secure, auditable, and consistent way to manage Airflow deployments — without direct access to the metadata database.
- **Task SDK**: The Task SDK provides python-native interfaces for defining DAGs, executing tasks in isolated subprocesses and interacting with Airflow resources (e.g., Connections, Variables, XComs, Metrics, Logs, and OpenLineage events) at runtime. The goal of task-sdk is to decouple DAG authoring from Airflow internals (Scheduler, API Server, etc.), providing a forward-compatible, stable interface for writing and maintaining DAGs across Airflow versions.
- **Docker stack**
- **Helm Chart**
- **Python API Client**: Airflow releases official Python API client that can be used to easily interact with Airflow REST API from Python code.
- **Providers packages**: Providers packages include integrations with third party projects. They are versioned and released independently of the Apache Airflow core.
