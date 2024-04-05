package debounce

import (
	"context"
	"strconv"
	"sync"
	"testing"
	"time"
)

type KeyValueType int

var key KeyValueType

var func1 Circuit = func(ctx context.Context) (string, error) {
	v, ok := ctx.Value(key).(int)
	if !ok {
		return "0", nil
	}
	return strconv.Itoa(v), nil
}

func Test_DebounceFirst(t *testing.T) {
	first := DebounceFirst(func1, time.Second*2)
	resp, err := first(context.WithValue(context.Background(), key, 1))
	t.Log(resp, err)

	resp, err = first(context.WithValue(context.Background(), key, 2))
	t.Log(resp, err)
}

func Test_DebounceLast(t *testing.T) {
	t.Log("start")
	last := DebounceLast(func1, time.Second)

	wg := sync.WaitGroup{}

	var results []string
	for cnt := 1; cnt <= 10; cnt++ {
		wg.Add(1)

		go func(cnt int) {
			defer wg.Done()

			resp, err := last(context.WithValue(context.Background(), key, cnt))
			t.Log(resp, err)
			results = append(results, resp)
		}(cnt)
	}

	wg.Wait()
	var canceldCnt = 0
	var cnt = 0
	for _, res := range results {
		if res == "cancel-default-value" {
			canceldCnt++
		} else {
			cnt++
		}
	}
	if cnt != 1 {
		t.Error("invalid result")
	}

}
