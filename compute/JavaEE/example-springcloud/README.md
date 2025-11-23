# Spring Cloud Examples

| Dependency                                                                         | Version         |
|:-----------------------------------------------------------------------------------|:----------------|
| Java                                                                               | 17              |
| Guava                                                                              | 33.4.0-jre      |
| Spring Boot                                                                        | 3.4.0           |
| Spring Cloud                                                                       | 2024.0.0, 4.2.0 |
| Spring Framework                                                                   | 6.2.0           |
| Spring Security                                                                    | 6.4.1           |
| Spring Authorization Server                                                        | 1.4.0           |
| Spring Session Data Redis                                                          | 3.4.0           |
| Spring Cloud Alibaba                                                               | 2023.0.3.2      |
| Spring Cloud OpenFeign                                                             | 4.2.0           |
| Spring Cloud LoadBalancer                                                          | 4.2.0           |
| Spring Cloud CircuitBreaker                                                        | 3.2.0           |
| Spring Cloud Config                                                                | 4.2.0           |
| Spring Cloud Netflix                                                               | 4.2.0           |
| Spring Cloud Gateway                                                               | 4.2.0           |
| Spring Cloud Function                                                              | 4.2.0           |
| Spring Cloud Stream                                                                | 4.2.0           |
| MySQL Connector J                                                                  | 8.0.32          |
| HSQL                                                                               | 2.7.4           |
| Testcontainers                                                                     | 1.20.6          |
| [R2DBC MySQL](https://github.com/asyncer-io/r2dbc-mysql)                           | 1.4.0           |
| [OkHttp MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver] | 4.12.0          |
| Project Reactor                                                                    | 3.7.0           |
| RxJava                                                                             | 3.1.10          |
| ShardingSphere-JDBC                                                                | 5.5.2           |
| [Micrometer](https://micrometer.io/)                                               | 1.14.1          |
| Dubbo                                                                              | 3.3.4           |
| Sentinel                                                                           | 1.8.8           |

| Example                     | Port         | Description                                                                                                       |
|:----------------------------|:-------------|:------------------------------------------------------------------------------------------------------------------|
| security                    | 10000        | spring-boot-starter-security                                                                                      |
| oauth2-authorization-server | 19000        | spring-boot-starter-oauth2-authorization-server                                                                   |
| oauth2-resource-server      | 18000        | spring-boot-starter-oauth2-resource-server                                                                        |
| oauth2-resource-client      | 18001        | spring-boot-starter-oauth2-client                                                                                 |
| keycloak-resource-server    | 18002        | spring-boot-starter-oauth2-resource-server, keycloak-policy-enforcer, keycloak-admin-client                       |
| keycloak-resource-client    | 18003        | spring-boot-starter-oauth2-client                                                                                 |
| admin-server                | 18004        | spring-boot-admin-starter-server                                                                                  |
| admin-client                | 18005        | spring-boot-admin-starter-client, spring-boot-starter-actuator                                                    |
| nacos-client                | 18006        | spring-cloud-starter-alibaba-nacos-config, spring-cloud-starter-alibaba-nacos-discovery                           |
| nacos-discovery             | 18007        | spring-cloud-starter-alibaba-nacos-discovery, spring-cloud-starter-openfeign                                      |
| seata                       | 18008        | seata-spring-boot-starter                                                                                         |
| springdoc-openapi           | 18009        | springdoc-openapi-starter-webmvc-api, springdoc-openapi-starter-webmvc-ui                                         |
| spring-core                 | 18010        | spring-boot-starter-web                                                                                           |
| mybatis                     | 18011        | mybatis-spring-boot-starter, druid-spring-boot-3-starter                                                          |
| config-server               | 18012        | spring-cloud-config-server, spring-cloud-config-monitor                                                           |
| config-client               | 18013        | spring-cloud-starter-config                                                                                       |
| eureka-server               | 18014        | spring-cloud-starter-netflix-eureka-server                                                                        |
| eureka-instance             | 18015        | spring-cloud-starter-netflix-eureka-client                                                                        |
| eureka-client               | 18015        | spring-cloud-starter-netflix-eureka-client                                                                        |
| docker                      | 18016        | spring-boot-maven-plugin, Dockerfile                                                                              |
| buildpacks                  | 18017, 18018 | spring-boot-maven-plugin                                                                                          |
| jib                         | 18019        | jib-maven-plugin                                                                                                  |
| webmvc                      | 18020        | spring-boot-starter-web, spring-boot-starter-data-jdbc, testcontainers                                            |
| webflux                     | 18021        | spring-boot-starter-webflux, spring-boot-starter-data-r2dbc, testcontainers, reactor-test                         |
| gateway-server              | 18022        | spring-cloud-starter-gateway, spring-cloud-starter-circuitbreaker-reactor-resilience4j, spring-session-data-redis |
| function                    | 18023        | spring-cloud-function-context                                                                                     |
| stream-processor            | 18024        | spring-cloud-stream-binder-rabbit                                                                                 |
| stream-producer             | 18025        | spring-cloud-stream-binder-rabbit                                                                                 |
| stream-consumer             | 18026        | spring-cloud-stream-binder-rabbit                                                                                 |
| reactor                     | 18027        | reactor-bom                                                                                                       |
| grafana                     | 18028        | loki-logback-appender, micrometer-registry-prometheus                                                             |
| skywalking                  | 18029        | apm-toolkit-logback-1.x                                                                                           |
| shardingsphere              | 18030        | shardingsphere-jdbc                                                                                               |
| sentinel                    | 18031,18032  | sentinel-core                                                                                                     |

# IP

localhost:

- 127.0.0.1
- authz.com
- 192.168.0.105

# Doc

- [Framework](./doc/Framework.md)
- [Configuration](./doc/Configuration.md)
- [Admin](./doc/Admin.md)
- [Persistent](./doc/Persistent.md)
- [Auth](./doc/Auth.md)
- [Service](./doc/Service.md)