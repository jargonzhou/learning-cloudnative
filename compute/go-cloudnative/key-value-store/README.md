# key-value-store

Features:

- Memory-based key value store.
- Transaction log: file, PostgreSQL.
- Web server: gorilla.
- Logging: zap.
- Configuration: YAML.

## kickoff

```shell
go mod init spike.com/key-value-store
go mod tidy

go run .
```

```shell
curl --request PUT \
  --url http://127.0.0.1:8888/v1/key-a \
  --header 'Content-Type: text/plain; charset=utf-8' \
  --data 'Hello, key-value store!'

curl --request GET --url http://127.0.0.1:8888/v1/key-a
Hello, key-value store!

curl --request DELETE --url http://127.0.0.1:8888/v1/key-a

curl --request GET --url http://127.0.0.1:8888/v1/key-a
no suck key
```

## Containerize

```shell
docker build -t spike.com/kvs .

docker run -it -p 8888:8888 -v ./transaction.log:/transaction.log spike.com/kvs
```

## Doc

TODO: add godoc comments.

```shell
go install  golang.org/x/tools/cmd/godoc@latest
godoc -http=:6060
```