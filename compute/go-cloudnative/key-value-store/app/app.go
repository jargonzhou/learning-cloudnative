package app

import (
	"fmt"
	"log"

	"spike.com/key-value-store/config"
	"spike.com/key-value-store/kvs"
)

func Start(addr string) {
	memoryKVStore, _ := kvs.NewMemoryKVStore()

	logger, err := initializeTransactionLog(&memoryKVStore)
	if err != nil {
		log.Fatalf("Initialize transaction logger failure: %s", err.Error())
		return
	}
	logger.Run()

	startRestServer(addr, &memoryKVStore, &logger)
}

func initializeTransactionLog(memoryKVStore *kvs.MemoryKVStore) (kvs.TransactionLogger, error) {
	var logger kvs.TransactionLogger
	var err error

	switch config.AppConfig.Logger {
	case "file":
		logger, err = kvs.NewFileTransactionLogger("transaction.log")
	case "pg":
		logger, err = kvs.NewPgTransactionLogger(
			config.AppConfig.Pg.Host,
			config.AppConfig.Pg.DbName,
			config.AppConfig.Pg.User,
			config.AppConfig.Pg.Password)
	default:
		log.Fatalf("Invalid logger type: %v\n", config.AppConfig.Logger)
	}

	if err != nil {
		return nil, fmt.Errorf("failed to create event logger: %w", err)
	}

	events, errors := logger.ReadEvents()
	e, ok := kvs.Event{}, true

	for ok && err == nil {
		select {
		case err, ok = <-errors:
		case e, ok = <-events:
			switch e.EventType {
			case kvs.EventPut:
				err = memoryKVStore.Put(e.Key, e.Value)
			case kvs.EventDelete:
				err = memoryKVStore.Delete(e.Key)
			}
		}
	}

	return logger, nil
}
