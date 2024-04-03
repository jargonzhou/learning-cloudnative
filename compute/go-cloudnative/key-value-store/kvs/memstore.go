package kvs

import (
	"sync"

	"go.uber.org/zap"
	"spike.com/key-value-store/config"
)

type MemoryKVStore struct {
	sync.RWMutex
	m map[string]string
}

func NewMemoryKVStore() (MemoryKVStore, error) {
	return MemoryKVStore{
		m: make(map[string]string),
	}, nil
}

func (s *MemoryKVStore) Put(key string, value string) error {
	s.Lock()
	config.Zaplog.Debug("Put", zap.String("key", key), zap.String("value", value))
	s.m[key] = value
	s.Unlock()
	return nil
}

func (s *MemoryKVStore) Get(key string) (string, error) {
	s.RLock()
	value, ok := s.m[key]
	s.RUnlock()
	if !ok {
		return "", ErrorNoSuchKey
	}
	return value, nil
}

func (s *MemoryKVStore) Delete(key string) error {
	s.Lock()
	config.Zaplog.Debug("Delete", zap.String("key", key))
	delete(s.m, key)
	s.Unlock()
	return nil
}
