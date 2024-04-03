package config

import (
	"fmt"
	"testing"

	"gopkg.in/yaml.v3"
)

func Test_Parse(t *testing.T) {
	config, err := parse("../kvs.yaml")

	if err != nil {
		t.Error("parse failed", err)
	} else {
		data, _ := yaml.Marshal(config)
		fmt.Println(string(data))
	}
}
