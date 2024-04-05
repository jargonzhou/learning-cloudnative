package cache

import (
	"testing"
)

func Test_Cache(t *testing.T) {
	cache.Add(1, "a")
	cache.Add(2, "b")

	v, ok := cache.Get(1)
	if !ok && v != "a" {
		t.Error("Invalid")
	}

	// trigger eviction
	cache.Add(3, "c")
	v, ok = cache.Get(2)
	if ok && v != nil {
		t.Error("Invalid")
	}
}
