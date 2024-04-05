package kvs

import (
	"errors"
	"log"

	"go.uber.org/zap"
	"spike.com/key-value-store/config"
)

type KVS struct {
	KVStore
	TransactionLogger

	*config.AppConfig
}

func (k *KVS) Start() {
	k.TransactionLogger.Run()
}

func NewKVS(cfg *config.AppConfig) (kvs KVS, err error) {
	kvs.AppConfig = cfg

	var kvsStore KVStore
	var logger TransactionLogger
	kvsStore, err = newKVStore(cfg)
	if err != nil {
		log.Println(err)
		return kvs, err
	}
	kvs.KVStore = kvsStore

	logger, err = newTransactionLogger(cfg)
	if err != nil {
		log.Println(err)
		return kvs, err
	}
	kvs.TransactionLogger = logger

	events, errors := logger.ReadEvents()
	e, ok := Event{}, true
	for ok && err == nil {
		select {
		case err, ok = <-errors:
		case e, ok = <-events:
			switch e.EventType {
			case EventPut:
				err = kvsStore.Put(e.Key, e.Value)
			case EventDelete:
				err = kvsStore.Delete(e.Key)
			}
		}
	}

	return kvs, nil
}

// store

var ErrorNoSuchKey = errors.New("no suck key")

type KVStore interface {
	Put(key string, value string) error
	Get(key string) (string, error)
	Delete(key string) error
}

func newKVStore(cfg *config.AppConfig) (KVStore, error) {
	return newMemoryKVStore(cfg)
}

// logger

type Event struct {
	Sequence  uint64    `json:"s"`
	EventType EventType `json:"t"`
	Key       string    `json:"k"`
	Value     string    `json:"v"`
}

type EventType byte

const (
	_                     = iota // 0
	EventDelete EventType = iota // 1
	EventPut
)

type TransactionLogger interface {
	WritePut(key, value string)
	WriteDelete(key string)
	Err() <-chan error

	ReadEvents() (<-chan Event, <-chan error)

	Run()
}

func newTransactionLogger(cfg *config.AppConfig) (l TransactionLogger, err error) {
	switch cfg.Transact.Type {
	case "file":
		l, err = newFileTransactionLogger(cfg)
	case "pg":
		l, err = newPgTransactionLogger(cfg)
	default:
		cfg.Log.Error("Invalid logger type", zap.String("type", cfg.Transact.Type))
	}
	return l, err
}
