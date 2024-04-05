// An example from '4. Cloud Native Patterns' in 'Cloud Native Go'.
package fanin

import "sync"

// Sources: A set of one or more input channels with the same type.
// Desitination: An output channel of the same type as Source.
// Funnel: Accepts Sources and immediately return Destination.

func Funnel(sources ...<-chan int) <-chan int {
	dest := make(chan int)

	var wg sync.WaitGroup
	wg.Add(len(sources))

	for _, ch := range sources {
		go func(c <-chan int) {
			// notify when c close
			defer wg.Done()

			for n := range c {
				dest <- n
			}
		}(ch)
	}

	// close dest after all sources close
	go func() {
		wg.Wait()
		close(dest)
	}()

	return dest
}
