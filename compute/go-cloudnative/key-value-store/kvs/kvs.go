package kvs

import (
	"errors"
)

// store

var ErrorNoSuchKey = errors.New("no suck key")

type KVStore interface {
	Put(key string, value string) error
	Get(key string) (string, error)
	Delete(key string) error
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
