package kvs

import (
	"sync"

	"go.uber.org/zap"
	"spike.com/key-value-store/config"
)

type MemoryKVStore struct {
	sync.RWMutex
	m map[string]string

	*config.AppConfig
}

func newMemoryKVStore(cfg *config.AppConfig) (KVStore, error) {
	return &MemoryKVStore{
		m:         make(map[string]string),
		AppConfig: cfg,
	}, nil
}

func (s *MemoryKVStore) Put(key string, value string) error {
	s.Lock()
	s.Log.Debug("Put", zap.String("key", key), zap.String("value", value))
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
	s.Log.Debug("Delete", zap.String("key", key))
	delete(s.m, key)
	s.Unlock()
	return nil
}
