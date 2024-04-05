package main

import (
	"spike.com/key-value-store/app"
)

// TODO: add gRPC support in 'Remote Procedure Calls with gRPC' in '8. Loose Coupling' in 'Cloud Native Go'.
// TODO: use the 'Hexagonal Architecture' in '8. Loose Coupling' in 'Cloud Native Go'.
// TODO: add client support for kvs.
func main() {
	app.Start()
}
