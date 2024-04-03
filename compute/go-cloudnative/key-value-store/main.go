package main

import (
	"spike.com/key-value-store/app"
	"spike.com/key-value-store/config"
)

func main() {
	config.Init()

	app.Start(":8888")
}
