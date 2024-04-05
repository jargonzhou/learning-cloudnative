# Plugins

## kickoff

```shell
go mod init spike.com/plugins
go mod tidy

go run .
```

```shell
# Windows: use WSL
$ go build -buildmode=plugin -o duck/duck.so duck/duck.go
-buildmode=plugin not supported on windows/amd64
$ file duck/duck.so
duck/duck.so: ELF 64-bit LSB shared object, x86-64, version 1 (SYSV), dynamically linked, BuildID[sha1]=ed30901fa6103bf6fc17013e7a457260e6c1a04e, with debug_info, not stripped

$ go build -buildmode=plugin -o frog/frog.so frog/frog.go
$ file frog/frog.so
frog/frog.so: ELF 64-bit LSB shared object, x86-64, version 1 (SYSV), dynamically linked, BuildID[sha1]=f71c4196a8d1b73d8f2319a51b54a5ee21ef7aa2, with debug_info, not stripped
```

```shell
$ go run main/main.go duck
A duck says: "quack".

$ go run main/main.go frog
A frog says: "ribbit".

$ go run main/main.go fox
plugin.Open("./fox/fox.so"): realpath failed
exit status 1
```