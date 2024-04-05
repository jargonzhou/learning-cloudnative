// An example from '4. Cloud Native Patterns' in 'Cloud Native Go'.
package retry

import (
	"context"
	"log"
	"time"
)

// The function that interacts with the service.
type Effector func(context.Context) (string, error)

// A function that accepts Effector and return a clouse with the same function signature.
func Retry(effector Effector, retries int, delay time.Duration) Effector {
	return func(ctx context.Context) (string, error) {
		for r := 0; ; r++ {
			response, err := effector(ctx)
			if err == nil || r >= retries {
				return response, err
			}

			log.Printf("Attemp %d faield, retrying in %v", r+1, delay)

			select {
			case <-time.After(delay):
			case <-ctx.Done():
				return "default-value", ctx.Err()
			}
		}
	}
}
