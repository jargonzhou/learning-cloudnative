package future

import (
	"context"
	"testing"
	"time"
)

func Test_Future(t *testing.T) {
	ctx := context.Background()
	f := SlowFunction(ctx)

	res, err := f.Result()
	if err != nil {
		t.Error(err)
	}
	t.Log(res)
}

func Test_FutureCancel(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()
	f := SlowFunction(ctx)

	res, err := f.Result()
	if err != context.DeadlineExceeded || res != "" {
		t.Error("invalid result")
	}
}
