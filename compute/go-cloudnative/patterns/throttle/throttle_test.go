package throttle

import (
	"context"
	"strconv"
	"sync"
	"testing"
	"time"
)

type KeyValueType int

var key KeyValueType

var effector Effector = func(ctx context.Context) (string, error) {
	v, ok := ctx.Value(key).(int)
	if !ok {
		return strconv.Itoa(0), nil
	} else {
		return strconv.Itoa(v), nil
	}
}

func TestThrottle(t *testing.T) {
	// func BenchmarkThrottle(t *testing.B) {

	throttle := Throttle(effector, 3, 3, time.Second)

	wg := sync.WaitGroup{}

	var results []string
	for i := 1; i <= 4; i++ {
		wg.Add(1)

		go func(idx int) {
			defer wg.Done()

			resp, err := throttle(context.WithValue(context.Background(), key, idx))
			t.Log(resp, err)
			results = append(results, resp)
		}(i)
	}
	wg.Wait()

	cnt := 0
	for _, r := range results {
		if r == "" {
			cnt++
		}
	}
	if cnt != 1 {
		t.Error("invalid result")
	}

	time.Sleep(time.Second)

	for i := 1; i <= 4; i++ {
		wg.Add(1)

		go func(idx int) {
			defer wg.Done()

			resp, err := throttle(context.WithValue(context.Background(), key, idx))
			t.Log(resp, err)
		}(i)
	}
	wg.Wait()

	cnt = 0
	for _, r := range results {
		if r == "" {
			cnt++
		}
	}
	if cnt != 1 {
		t.Error("invalid result")
	}
}
