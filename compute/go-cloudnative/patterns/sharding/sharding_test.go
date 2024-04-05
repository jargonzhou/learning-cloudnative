package sharding

import (
	"sort"
	"strings"
	"testing"
)

func Test_ShardedMap(t *testing.T) {
	sm := NewShardedMap(5)
	sm.Set("a", 1)
	sm.Set("b", 2)
	sm.Set("c", 3)

	if sm.Get("a").(int) != 1 {
		t.Error("invalid result")
	}
	sm.Delete("a")
	if nil != sm.Get("a") {
		t.Error("invalid result")
	}

	keys := sm.Keys()
	sort.Strings(keys)
	if strings.Join(keys, "") != "bc" {
		t.Error("invalid result")
	}
}
