// An example from '4. Cloud Native Patterns' in 'Cloud Native Go'.
package debounce

import (
	"context"
	"sync"
	"time"
)

// The function to regulate.
type Circuit func(context.Context) (string, error)

// A clouse with the same function signature as Circuit: function-first.
func DebounceFirst(circuit Circuit, d time.Duration) Circuit {
	var threshold time.Time
	var result string
	var err error
	var m sync.Mutex

	return func(ctx context.Context) (string, error) {
		m.Lock()
		defer func() {
			threshold = time.Now().Add(d)
			m.Unlock()
		}()

		if time.Now().Before(threshold) {
			return result, err
		}

		// issue the request
		result, err = circuit(ctx)
		return result, err
	}
}

// function-last.
func DebounceLast(circuit Circuit, d time.Duration) Circuit {
	var m sync.Mutex
	var timer *time.Timer
	var cctx context.Context
	var cancel context.CancelFunc

	return func(ctx context.Context) (string, error) {
		m.Lock()

		if timer != nil {
			timer.Stop()
			cancel()
		}

		cctx, cancel = context.WithCancel(ctx)
		ch := make(chan struct {
			result string
			err    error
		}, 1)

		timer = time.AfterFunc(d, func() {
			// issue the request
			r, e := circuit(cctx)
			ch <- struct {
				result string
				err    error
			}{r, e}
		})

		m.Unlock()

		select {
		case res := <-ch:
			return res.result, res.err
		case <-cctx.Done():
			return "cancel-default-value", cctx.Err()
		}
	}
}
