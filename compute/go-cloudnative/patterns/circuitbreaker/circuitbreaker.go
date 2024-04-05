// An example from '4. Cloud Native Patterns' in 'Cloud Native Go'.
package circuitbreaker

import (
	"context"
	"errors"
	"sync"
	"time"
)

// The function that interacts with the service.
type Circuit func(context.Context) (string, error)

// A clouse with the same function signature as Circuit.
func Breaker(circuit Circuit, failureThreashold uint) Circuit {
	var consecutiveFailures int = 0
	var lastAttemp = time.Now()
	var m sync.RWMutex

	return func(ctx context.Context) (string, error) {
		m.RLock()
		d := consecutiveFailures - int(failureThreashold)
		if d >= 0 && failureThreashold != 0 {
			shouldRetryAt := lastAttemp.Add(time.Second * 2 << d)
			if !time.Now().After(shouldRetryAt) {
				m.RUnlock()
				return "", errors.New("service unreachable")
			}
		}
		m.RUnlock()

		// issuse the request
		response, err := circuit(ctx)
		// lock around shared resource
		m.Lock()
		defer m.Unlock()

		lastAttemp = time.Now()
		if err != nil {
			consecutiveFailures++
			return response, err
		}
		// no error occurs, reset failure counter
		consecutiveFailures = 0
		return response, nil
	}
}
