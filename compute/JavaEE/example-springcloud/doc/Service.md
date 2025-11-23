# nacos-discovery

```shell
# 不启动nacos-config
curl --request GET \
  --url http://127.0.0.1:18007/config/get
# 响应:
{}

# 启动nacos-config
curl --request GET \
  --url http://127.0.0.1:18007/config/get
# 响应:
{
  "commonUserLocalCache": false,
  "userLocalCache": false
}
```

# Netflix

## eureka-server

Dashboard: `http://127.0.0.1:18014/`.

## eureka-client

# Dubbo

```shell
curl \
    --header "Content-Type: application/json" \
    --data '["Dubbo"]' \
    http://localhost:50051/com.spike.alibaba.dubbo.service.DemoService/sayHello
# 响应:
"Hello Dubbo, response from provider."
```
