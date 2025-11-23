# jpa-h2

H2 Console: `http://localhost:18011/h2-console`.

# mybatis

MyBatis Generator: `com.spike.cloud.persistent.mybatis.Generator`.

- Model, Mapper, XML, Example.
- lombok.

```shell
curl --request POST \
  --url http://127.0.0.1:18011/api/user
# 响应:
1
# output:
DEBUG 8972 --- [io-18011-exec-1] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped to com.spike.cloud.persistent.mybatis.web.UserController#postUser()
Creating a new SqlSession
Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@61c9304f]
JDBC Connection [ConnectionProxyImpl{connectedTime=xxxx-yy-mm 21:41:27.044, closeCount=0, lastValidateTimeMillis=xxxx-yy-mm 21:42:14.973}] will be managed by Spring
==>  Preparing: insert into users (email, password) values (?, ?)
==> Parameters: user@example.com(String), xxx(String)
<==    Updates: 1
==>  Preparing: SELECT LAST_INSERT_ID()
==> Parameters: 
<==    Columns: LAST_INSERT_ID()
<==        Row: 1
<==      Total: 1
Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@61c9304f]
com.spike.cloud.persistent.mybatis.domain.service.UserService.postUser
Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@61c9304f] from current transaction
==>  Preparing: update users set email = ?, password = ? where id = ?
==> Parameters: user@example.com(String), yyy(String), 1(Integer)
<==    Updates: 1
Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@61c9304f]
Transaction synchronization committing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@61c9304f]
Transaction synchronization deregistering SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@61c9304f]
Transaction synchronization closing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@61c9304f]
```

Druid Monitor: `http://127.0.0.1:18011/druid`.

- test cases: shutdown MySQL, then turn on.

# seata

```shell
# init
mysql> select * from account_tbl;
+----+---------+-------+
| id | user_id | money |
+----+---------+-------+
| 10 | U100001 | 10000 |
+----+---------+-------+

# success
curl --request GET \
  --url 'http://127.0.0.1:18008/api/txn?mockFail=false'
ok

mysql> select * from account_tbl;
+----+---------+-------+
| id | user_id | money |
+----+---------+-------+
| 10 | U100001 |  9800 |
+----+---------+-------+
  
# fail
curl --request GET \
  --url 'http://127.0.0.1:18008/api/txn?mockFail=true'
{
  "timestamp": "...",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/txn"
}

mysql> select * from account_tbl;
+----+---------+-------+
| id | user_id | money |
+----+---------+-------+
| 10 | U100001 |  9800 |
+----+---------+-------+
```

# webmvc

- `@DataJdbcTest`, Testcontainers
- `@EnableJdbcAuditing`

# webflux

- `@DataR2dbcTest`, Testcontainers
- `@EnableR2dbcAuditing`

# shardingsphere

- `shardingsphere.sql`: init schema sql.
- `shardingsphere-jdbc.yaml`: configuration.
- `ShardingSphereTest`: tests.
