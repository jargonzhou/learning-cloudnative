# nacos-client

```shell
# 创建命名空间example
# 创建dataId: common.yaml, nacos-client.yaml
curl --request GET \
  --url http://127.0.0.1:18006/config/get
# 响应:
{
  "commonUserLocalCache": true,
  "userLocalCache": true
}
# 修改值为false
curl --request GET \
  --url http://127.0.0.1:18006/config/get
# 响应:
{
  "commonUserLocalCache": false,
  "userLocalCache": false
}
```

# Spring Cloud Config
## config-server

- `@EnableConfigServer`

使用本地文件系统backend. 目录结构: `app-name/application[-dev].yaml`.

```shell
curl --request GET \
  --url http://127.0.0.1:18012/config-client/dev/config-client
# 响应:
{
  "name": "config-client",
  "profiles": [
    "dev"
  ],
  "label": "config-client",
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "classpath:/config/config-client/application-dev.yaml",
      "source": {
        "info.name": "dev"
      }
    },
    {
      "name": "classpath:/config/config-client/application.yaml",
      "source": {
        "info.name": "default",
        "info.description": "Config client",
        "greeting": "Hello, there!",
        "app.name": "config-client",
        "app.description": "Client use config server"
      }
    }
  ]
}

# 编辑文件后重新查询即可生效
```

## config-client

- `@ConfigurationProperties`
- `@Value`
- `Environment`
- `@RefreshScope`

external configuration:
```shell
# command-line argument
--greeting=""
# JVM system properties
-Dgreeting=""
# environment variables
GREETING=""
```


```shell
curl --request GET \
  --url http://127.0.0.1:18013/config
# 响应:
{
  "description": "Config client",
  "name": "dev",
  "app": {
    "description": "Client use config server",
    "name": "config-client"
  },
  "greeting": "Hello, there!"
}
```

添加Actuator和`@RefreshScope`, 手动刷新配置:

```shell
curl --request POST \
  --url http://127.0.0.1:18013/actuator/refresh
# 响应:
[
  "greeting"
]

curl --request GET \
  --url http://127.0.0.1:18013/config
# 响应:
{
  "description": "Config client",
  "name": "dev",
  "app": {
    "description": "Client use config server",
    "name": "config-client"
  },
  "greeting": "Hello, there!!!"
}
```

## with spring-cloud-config-monitor

```shell
# 编辑文件: config-server/src/main/resources/config/config-client/application.yaml
#greeting: "Hello, there!"

# 重新获取配置
curl --request GET \
  --url http://127.0.0.1:18013/config
# 响应:
{
  "description": "Config client",
  "name": "dev",
  "app": {
    "description": "Client use config server",
    "name": "config-client"
  },
  "greeting": "Hello, there!"
}
```