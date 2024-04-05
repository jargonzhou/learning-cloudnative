package config

import (
	"log"
	"os"

	"go.uber.org/zap"
	"gopkg.in/yaml.v3"
)

type KVSConfig struct {
	Addr string `yaml:"addr"`

	Zap struct {
		LogLevel string `yaml:"log-level"`
	}
	Transact struct {
		Type string
		File struct {
			Name string `yaml:"name"`
		}
		Pg struct {
			Host     string `yaml:"host"`
			DbName   string `yaml:"dbname"`
			Schema   string `yaml:"schema"`
			Table    string `yaml:"table"`
			User     string `yaml:"user"`
			Password string `yaml:"password"`
		}
	}
	Logger string `yaml:"logger"`
}

type AppConfig struct {
	KVSConfig

	// can we put logger in another location???
	Log *zap.Logger
}

// make initialization explicitly
func Load() (AppConfig, error) {
	var appConfig AppConfig

	kvsConfig, err := parse("kvs.yaml")
	if err != nil {
		log.Printf("Can not parse kvs.yaml: %#v\n", err)
		return appConfig, err
	}
	appConfig.KVSConfig = kvsConfig

	var logger *zap.Logger
	switch kvsConfig.Zap.LogLevel {
	case "dev":
		logger, err = zap.NewDevelopment()
	case "prod":
		logger, err = zap.NewProduction()
	}
	if err != nil {
		log.Printf("Can not initialize zap logger: %#v\n", err)
		return appConfig, err
	}
	appConfig.Log = logger
	defer logger.Sync()
	logger.Info("zap logger initialized")

	logger.Debug("Application configuration", zap.Any("config", kvsConfig))
	return appConfig, nil
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
