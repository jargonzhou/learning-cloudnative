package timeouts

import (
	"context"
	"testing"
	"time"
)

var Slow SlowFunction = func(s string) (string, error) {
	time.Sleep(time.Millisecond * 1100)
	return "ok", nil
}

func TestTimeOut(t *testing.T) {
	ctx := context.Background()
	cctx, cancel := context.WithTimeout(ctx, time.Second)
	defer cancel()

	timeout := Timeout(Slow)
	res, err := timeout(cctx, "input")
	t.Log(res, err)
	if res == "ok" {
		t.Error("invalid result")
	}
	if err != context.DeadlineExceeded {
		t.Error(err)
	}
}
