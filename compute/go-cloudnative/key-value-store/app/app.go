package app

import (
	"log"

	"spike.com/key-value-store/config"
	"spike.com/key-value-store/kvs"
)

func Start() {
	cfg, err := config.Load()
	if err != nil {
		log.Fatalf("Load configuration failure: %s", err.Error())
	}

	kvs, err := kvs.NewKVS(&cfg)
	if err != nil {
		log.Fatalf("Initialize KVS failure: %s", err.Error())
	}
	kvs.Start()

	server, err := NewRestServer(kvs)
	if err != nil {
		log.Fatalf("Initialize REST server failure: %s", err.Error())
	}
	server.Start(cfg.Addr)
}
