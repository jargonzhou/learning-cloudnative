# spring-core

- `@Import(WorkerRegistrar.class)`
```shell
curl -H "Content-Type: application/json" -X POST http://localhost:18010/workers/run/HelloWorld
{"success":true,"data":"HelloWorld: Spike","message":null}
```

- CDS(Class Data Sharing)
```shell
$ java --version
openjdk 17.0.9 2023-10-17
OpenJDK Runtime Environment Temurin-17.0.9+9 (build 17.0.9+9)
OpenJDK 64-Bit Server VM Temurin-17.0.9+9 (build 17.0.9+9, mixed mode, sharing)
# after mvn package, extract to CDS friendly layout: lib, *.jar
$ java -Djarmode=tools -jar spring-core-0.0.1-SNAPSHOT.jar extract --destination application
# run
$ java -jar application/spring-core-0.0.1-SNAPSHOT.jar

# create CDS cache
$ cd application/
$ java -XX:ArchiveClassesAtExit=application.jsa -Dspring.context.exit=onRefresh -jar spring-core-0.0.1-SNAPSHOT.jar
# run
$ java -XX:ArchiveClassesAtExit=application.jsa -jar spring-core-0.0.1-SNAPSHOT.jar
```

- AOT(Ahead-of-Time) Processing
```shell
# 注释掉@Import(WorkerRegistrar.class)
$ mvn package -P native
$ java -Dspring.aot.enabled=true -jar spring-core-0.0.1-SNAPSHOT.jar
SpringCoreApplication    : Starting AOT-processed SpringCoreApplication v0.0.1-SNAPSHOT
```

- GraalVM Native Application
```shell
$ ./native-image.cmd --version
native-image 17.0.9 2023-10-17
GraalVM Runtime Environment GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22)
Substrate VM GraalVM CE 17.0.9+9.1 (build 17.0.9+9, serial gc)

# Error: On Windows, GraalVM Native Image for JDK 17 requires Visual Studio 2022 version 17.1.0 or later (C/C++ Optimizing Compiler Version 19.31 or later).
# use: x64 Native Tools Command Prompt for VS 2022
# or: https://github.com/oracle/graal/issues/7121
$ mklink /d "C:\Program Files\Microsoft Visual Studio" "D:\software\Microsoft Visual Studio"
symbolic link created for C:\Program Files\Microsoft Visual Studio <<===>> D:\software\Microsoft Visual Studio

$ mvn native:compile -P native

# run
$ .\spring-core.exe

# use PowerShell
$  Measure-Command {".\spring-core.exe"}


Days              : 0
Hours             : 0
Minutes           : 0
Seconds           : 0
Milliseconds      : 7
Ticks             : 75615
TotalDays         : 8.75173611111111E-08
TotalHours        : 2.10041666666667E-06
TotalMinutes      : 0.000126025
TotalSeconds      : 0.0075615
TotalMilliseconds : 7.5615
```

# reactor



# webmvc

- `@SpringBootTest`
- `Validator`
- `@WebMvcTest`, `MockMvc`
- `@JsonTest`
- `@DataJdbcTest`, Testcontainers
- `@EnableJdbcAuditing`

# webflux

- `@WebFluxTest`, `WebTestClient`
- `@DataR2dbcTest`, Testcontainers
- `@EnableR2dbcAuditing`
- `MockWebServer`, Reactor Test `StepVerifier`

# gateway-server

- webmvc: NOT RUNNING
```shell
# use WSL Ubuntu
$ curl -s http://192.168.3.178:18022/books
<FALLBACK>%
# ab - Apache HTTP server benchmarking tool
$ ab -n 21 -c 1 -m GET http://192.168.3.178:18022/books
Complete requests:      21
Failed requests:        0
# log:
...
CircuitBreakerStateMachine     : Event ERROR published: : CircuitBreaker 'webmvc-cb' recorded an error: 'io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: no further information: localhost/127.0.0.1:18020'. Elapsed time: 1 ms
CircuitBreakerStateMachine     : Event FAILURE_RATE_EXCEEDED published: : CircuitBreaker 'webmvc-cb' exceeded failure rate threshold. Current failure rate: 100.0
CircuitBreakerStateMachine     : Event STATE_TRANSITION published: : CircuitBreaker 'webmvc-cb' changed state from CLOSED to OPEN
RouterFunctionMapping          : [6b5b17cd-41] Mapped to com.spike.springcloud.gateway.web.WebEndpoints$$Lambda$569/0x000001cf3e42f170@41dc3bcb
...
CircuitBreakerStateMachine     : Event NOT_PERMITTED published: : CircuitBreaker 'webmvc-cb' recorded a call which was not permitted.
```
- webmvc: RUNNING
```shell
$ ab -n 21 -c 1 -m GET http://192.168.3.178:18022/books
Complete requests:      21
Failed requests:        0
```

- webflux: NOT RUNNING
```shell
$ curl -s http://192.168.3.178:18022/orders
{"timestamp":"2025-03-11T02:57:25.436+00:00","path":"/orders","status":500,"error":"Internal Server Error","requestId":"f4bca7f1-23"}

$ ab -n 21 -c 1 -m GET http://192.168.3.178:18022/orders
Complete requests:      21
Non-2xx responses:      21
# log:
CircuitBreakerStateMachine     : Event ERROR published: : CircuitBreaker 'webflux-cb' recorded an error: 'io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: no further information: localhost/127.0.0.1:18021'. Elapsed time: 3 ms
CircuitBreakerStateMachine     : Event FAILURE_RATE_EXCEEDED published: : CircuitBreaker 'webflux-cb' exceeded failure rate threshold. Current failure rate: 100.0
CircuitBreakerStateMachine     : Event STATE_TRANSITION published: : CircuitBreaker 'webflux-cb' changed state from CLOSED to OPEN
CircuitBreakerStateMachine     : Event NOT_PERMITTED published: : CircuitBreaker 'webflux-cb' recorded a call which was not permitted.
```

- webflux: RUNNING
```shell
$ ab -n 21 -c 1 -m GET http://192.168.3.178:18022/orders
Complete requests:      21
Failed requests:        0
```

- `RedisRateLimiter`
```shell
$ ab -c 20 -t 30 -m GET http://192.168.3.178:18022/books
Complete requests:      3137
Failed requests:        3067
# log:
...
HttpWebHandlerAdapter    : Completed 429 TOO_MANY_REQUESTS
```

- `SaveSession`
```shell
$ redis-cli
127.0.0.1:6379> hgetall gateway-server:sessions:85c5c4df-bc6d-416d-9292-d0fb7e9e45f0
1) "creationTime"
2) "..."
3) "lastAccessedTime"
4) "..."
5) "maxInactiveInterval"
6) "..."
```

# function

```shell
curl --request GET \
  --url http://127.0.0.1:18023/actuator/functions
# 响应:
{
  "allQuotes": {
    "type": "SUPPLIER",
    "output-type": "quote"
  },
  "functionRouter": {
    "type": "FUNCTION",
    "input-type": "object",
    "output-type": "object"
  },
  "genreQuote": {
    "type": "FUNCTION",
    "input-type": "genre",
    "output-type": "quote"
  },
  "label": {
    "type": "FUNCTION",
    "input-type": "long",
    "output-type": "orderdispatchedmessage"
  },
  "logQuote": {
    "type": "CONSUMER",
    "input-type": "quote"
  },
  "pack": {
    "type": "FUNCTION",
    "input-type": "orderacceptedmessage",
    "output-type": "long"
  },
  "randomQuote": {
    "type": "SUPPLIER",
    "output-type": "quote"
  }
}
```

调用:
```shell
curl --request POST \
  --url http://127.0.0.1:18023/func/ \
  --header 'Content-Type: application/json' \
  --data '{
  "orderId": 1
}'
# 响应:
[
  {
    "orderId": 1
  }
]
```

- 函数组合
```shell
$ curl --request POST \
  --url 'http://127.0.0.1:18023/func/genreQuote,logQuote' \
  --header 'Content-Type: text/plain; charset=utf-8' \
  --data FANTASY
# 响应:
202 Accepted
```


# stream

RabbitMQ Exchanges:
- order-accepted
- order-dispatched

RabbitMQ Queues:
- order-accepted.stream-processor
- order-dispatched.stream-consumer

## stream-producer

- `StreamBridge`
- `@Transactional`, `@EnableTransactionManagement`

```shell
curl --request POST \
  --url http://127.0.0.1:18025/orders \
  --header 'Content-Type: application/json' \
  --data '{
  "orderId": 1234
}'
# 响应:
Ok
```

- 事务回滚

```shell
curl --request POST \
  --url 'http://127.0.0.1:18025/orders?rollback=true' \
  --header 'Content-Type: application/json' \
  --data '{
  "orderId": 1234
}'
# 响应:
{
  "timestamp": "2025-03-12T02:53:16.655+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/orders"
}
# stream-processor: no output
# stream-consumer: no output
```

```shell
OrderController         : Sending order accepted event with id: 1234
OrderController         : Result of sending data for order with id 1234: true
```

## stream-processor

- Function, RabbitMQ

```shell
pack                                     : The order with id 1234 is packed.
label                                    : The order with id 1234 is labeled.
```

## stream-consumer

- Function, RabbitMQ

```shell
dispatchOrder                            : The order with id 1234 is dispatched
```

# flyway

None yet.