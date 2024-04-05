# Hashicorp Plugins

## kickoff

```shell
$ go build -o ./duck/duck ./duck/duck.go

$ file ./duck/duck
./duck/duck: PE32+ executable (console) x86-64, for MS Windows, 15 sections

$ ./duck/duck 
This binary is a plugin. These are not meant to be executed directly.
Please execute the program that consumes these plugins, which will
load any plugins automatically

$ go run main/main.go duck
[DEBUG] plugin: starting plugin: path=./duck/duck args=[./duck/duck]
[DEBUG] plugin: plugin started: path=./duck/duck pid=6484
[DEBUG] plugin: waiting for RPC address: plugin=./duck/duck
[DEBUG] plugin.duck: plugin address: address=/tmp/plugin915114019 network=unix timestamp=xxx
[DEBUG] plugin: using plugin: version=1
A duck says: "Quack".
[DEBUG] plugin.duck: [DEBUG] plugin: plugin server: accept unix /tmp/plugin915114019: use of closed network connection
[INFO]  plugin: plugin process exited: plugin=./duck/duck id=6484
[DEBUG] plugin: plugin exited
```