// An example LRU cache from '7. Scalability' in 'Cloud Native Go'.
package cache

import (
	"fmt"

	// extension to golang/groupcache
	lru "github.com/hashicorp/golang-lru"
)

var cache *lru.Cache

func init() {
	cache, _ = lru.NewWithEvict(2, func(key interface{}, value interface{}) {
		fmt.Printf("Evicted: key=%#v, value=%#v\n", key, value)
	})
}
