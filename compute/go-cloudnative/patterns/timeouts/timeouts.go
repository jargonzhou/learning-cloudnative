// An example from '4. Cloud Native Patterns' in 'Cloud Native Go'.
package timeouts

import "context"

// The long-running function.
type SlowFunction func(string) (string, error)

// A wrapper function around SlowFunction that implemtnts the timeout logic.
type WithContext func(context.Context, string) (string, error)

func Timeout(f SlowFunction) WithContext {
	return func(ctx context.Context, arg string) (string, error) {
		resch := make(chan string)
		errch := make(chan error)

		go func() {
			// issue the request
			res, err := f(arg)
			resch <- res
			errch <- err
		}()

		select {
		case res := <-resch:
			return res, <-errch
		case <-ctx.Done():
			return "", ctx.Err()
		}
	}
}
