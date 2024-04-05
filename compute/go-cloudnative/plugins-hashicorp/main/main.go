package main

import (
	"fmt"
	"log"
	"os"
	"os/exec"

	"github.com/hashicorp/go-plugin"
	"spike.com/plugins-hashicorp/commons"
)

func main() {
	if len(os.Args) != 2 {
		log.Fatal("usage: run main/main.go animal")
	}
	name := os.Args[1]
	module := fmt.Sprintf("./%s/%s", name, name)

	_, err := os.Stat(module)
	if os.IsNotExist(err) {
		log.Fatal("can't find an animal named ", name)
	}

	// client
	var pluginMap = map[string]plugin.Plugin{
		"sayer": &commons.SayerPlugin{},
	}
	client := plugin.NewClient(&plugin.ClientConfig{
		HandshakeConfig: commons.HandshakeConfig,
		Plugins:         pluginMap,
		Cmd:             exec.Command(module),
	})
	defer client.Kill()

	rpcClient, err := client.Client()
	if err != nil {
		log.Fatal(err)
	}

	raw, err := rpcClient.Dispense("sayer")
	if err != nil {
		log.Fatal(err)
	}
	sayer := raw.(commons.Sayer)
	fmt.Printf("A %s says: %q.\n", name, sayer.Says())
}
