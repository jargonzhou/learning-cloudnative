# admin-server

未使用服务发现.

# admin-client

使用Actuator和客户端注册.

# springdoc-openapi

Swagger UI: http://localhost:18009/swagger-ui/index.html

# docker

```shell
$ mvn package
$ docker build -f Dockerfile -t docker:0.0.1-SNAPSHOT .
$ docker run -it --name docker -p 18016:18016 docker:0.0.1-SNAPSHOT

$ curl -s http://localhost:18016/
Hello, Docker!
```

# buildpacks

```shell
$ mvn spring-boot:build-image
$ docker run -it --name buildpacks \
  --env BPL_DEBUG_ENABLED=true \
  --env BPL_DEBUG_PORT=18018 \
  -p 18017:18017 \
  -p 18018:18018 \
  buildpacks:0.0.1-SNAPSHOT

$ curl -s http://localhost:18017/
Hello, Buildpacks!

# IDEA Remote JVM Debug
#-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:18018
```

# jib

```shell
# build to Docker daemon
$ mvn clean package jib:dockerBuild
$ docker run -it --name jib -p 18018:80 jib:latest --rm

$ curl -s http://localhost:18018/
Hello, Jib!
```

# grafana

Grafana Loki:
- Add datasource: Loki.
- Explore: loki

Prometheus metrics:
```shell
curl --request GET \
  --url http://127.0.0.1:18028/actuator/prometheus \
  --header 'Accept: application/openmetrics-text'
```

Tempo:
```shell
$ docker build -t eclipse-temurin:17-otel -f otel.Dockerfile .
$ docker build -t dev/grafana-app .
$ docker compose up -d
```

# skywalking

- `skywalking.Dockerfile`: Java image with agent.
- `logback.xml`: Logback with gRPC reporter.

```shell
$ docker build -t dev/skywalking-app .
$ docker compose up -d
```

# sentinel

```shell
# use Windows WSL
$ ab -n 21 -c 1 -m GET http://192.168.0.103:18031/books/hello/aaa
$ ab -n 21 -c 1 -m GET http://192.168.0.103:18031/books/world/bbb
```