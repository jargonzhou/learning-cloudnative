package retry

import (
	"context"
	"errors"
	"testing"
	"time"
)

type KeyValueType bool

var key KeyValueType

var effector Effector = func(ctx context.Context) (string, error) {
	v, ok := ctx.Value(key).(bool)
	if !ok || !v {
		return "", errors.New("fail")
	} else {
		return "success", nil
	}
}

func TestRetry(t *testing.T) {
	retry := Retry(effector, 1, time.Second)

	resp, err := retry(context.WithValue(context.Background(), key, false))
	t.Log(resp, err)
	if err == nil {
		t.Error("failed")
	}

	resp, err = retry(context.WithValue(context.Background(), key, true))
	t.Log(resp, err)
	if err != nil {
		t.Error(err)
	}
}
