package main

import (
	"github.com/hashicorp/go-plugin"
	"spike.com/plugins-hashicorp/commons"
)

type Duck struct{}

func (g *Duck) Says() string {
	return "Quack"
}

func main() {
	// service implementation
	sayer := &Duck{}

	var pluginMap = map[string]plugin.Plugin{
		"sayer": &commons.SayerPlugin{Impl: sayer},
	}

	plugin.Serve(&plugin.ServeConfig{
		HandshakeConfig: commons.HandshakeConfig,
		Plugins:         pluginMap,
	})

}
