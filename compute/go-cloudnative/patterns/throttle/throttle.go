// An example from '4. Cloud Native Patterns' in 'Cloud Native Go'.
package throttle

import (
	"context"
	"errors"
	"sync"
	"time"
)

// The function to regulate.
type Effector func(context.Context) (string, error)

// A function that accepts Effector and returns a closure with the same signature as Effector.
func Throttle(e Effector, max uint, refill uint, d time.Duration) Effector {
	var tokens = max
	var once sync.Once

	return func(ctx context.Context) (string, error) {
		if ctx.Err() != nil {
			return "", ctx.Err()
		}

		once.Do(func() {
			ticker := time.NewTicker(d)

			go func() {
				defer ticker.Stop()

				for {
					select {
					case <-ctx.Done():
						return
					case <-ticker.C:
						t := tokens + refill
						if t > max {
							t = max
						}
						tokens = t
					}
				}
			}()
		})

		if tokens <= 0 {
			return "", errors.New("too many calls")
		}

		// issue the request
		tokens--
		return e(ctx)
	}
}
