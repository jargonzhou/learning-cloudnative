package config

import (
	"log"
	"os"

	"go.uber.org/zap"
	"gopkg.in/yaml.v3"
)

type KVSConfig struct {
	Zap struct {
		LogLevel string `yaml:"log-level"`
	}

	Logger string `yaml:"logger"`
	Pg     struct {
		Host     string `yaml:"host"`
		DbName   string `yaml:"dbname"`
		User     string `yaml:"user"`
		Password string `yaml:"password"`
	}
}

var Zaplog *zap.Logger
var AppConfig KVSConfig

// make initialization explicitly
func Init() {
	var err error
	AppConfig, err = parse("kvs.yaml")
	if err != nil {
		log.Fatalf("Can not parse kvs.yaml: %#v", err)
	}

	switch AppConfig.Zap.LogLevel {
	case "dev":
		Zaplog, err = zap.NewDevelopment()
	case "prod":
		Zaplog, err = zap.NewProduction()
	}
	if err != nil {
		log.Fatalf("Can not initialize zap logger: %#v\n", err)
	}
	defer Zaplog.Sync()
	Zaplog.Info("zap logger initialized")

	Zaplog.Debug("Application configuration", zap.Any("config", AppConfig))
}

func parse(filename string) (KVSConfig, error) {
	var config KVSConfig

	data, err := os.ReadFile(filename)
	if err != nil {
		log.Fatalf("read file failed: %v\n", filename)
		return config, err
	}

	err = yaml.Unmarshal(data, &config)
	if err != nil {
		log.Fatalf("parse file failed: %v\n", filename)
		return config, err
	}

	return config, nil
}
