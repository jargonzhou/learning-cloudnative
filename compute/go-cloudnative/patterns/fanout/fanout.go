// An example from '4. Cloud Native Patterns' in 'Cloud Native Go'.
package fanout

// Sources: An input channel.
// Desitination: An output channel of the same type as Source.
// Split: A function that accepts Source and immediately returns Destinations.

func Split(source <-chan int, n int) []<-chan int {
	dests := make([]<-chan int, 0)

	// create n destination channels
	for i := 0; i < n; i++ {
		ch := make(chan int)
		dests = append(dests, ch)

		// each channel gets a goroutine to read from source
		go func() {
			defer close(ch)

			for v := range source {
				ch <- v
			}
		}()
	}

	return dests
}
