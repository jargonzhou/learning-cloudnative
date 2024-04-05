package circuitbreaker

import (
	"context"
	"errors"
	"fmt"
	"testing"
)

var func1 Circuit = func(ctx context.Context) (string, error) {
	return "hello", nil
}

var func2 Circuit = func(ctx context.Context) (string, error) {
	return "", errors.New("dead service")
}

type contextValueType string

const key contextValueType = "fail"

var func3 Circuit = func(ctx context.Context) (string, error) {
	fail, ok := ctx.Value(key).(bool)
	if !ok || fail {
		return "", errors.New("fail")
	} else {
		return "success", nil
	}
}

func Test_SuccessCall(t *testing.T) {
	breaker := Breaker(func1, 0)
	resp, err := breaker(context.Background())
	if err != nil {
		t.Error(err)
	}
	if resp != "hello" {
		t.Error(resp)
	}
}

func Test_FailureCall(t *testing.T) {
	breaker := Breaker(func2, 0)
	resp, err := breaker(context.Background())
	if err == nil || err.Error() != "dead service" {
		t.Error(resp)
	}
	if resp != "" {
		t.Error(err)
	}
}

func Test_FailureCallRetryFailure(t *testing.T) {
	breaker := Breaker(func3, 1)
	resp, err := breaker(context.Background())
	if err == nil || err.Error() != "fail" {
		t.Error(resp)
	}
	if resp != "" {
		t.Error(err)
	}

	// opened
	resp, err = breaker(context.Background())
	if err == nil || err.Error() != "service unreachable" {
		t.Error("Wrong error")
	}
	if resp != "" {
		t.Error(err)
	}
}

func Test_FailureCallRetrySuccess(t *testing.T) {
	breaker := Breaker(func3, 2)
	resp, err := breaker(context.WithValue(context.Background(), key, true))
	if err == nil || err.Error() != "fail" {
		t.Error(resp)
	}
	if resp != "" {
		t.Error(err)
	}

	// closed
	resp, err = breaker(context.WithValue(context.Background(), key, false))
	fmt.Println(resp)
	if err != nil {
		t.Error(err)
	}
}
