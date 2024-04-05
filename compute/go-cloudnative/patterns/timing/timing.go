// An example timer and ticker from '7. Scalability' in 'Cloud Native Go'.
package timing

import (
	"fmt"
	"time"
)

func timely() {
	timer := time.NewTimer(5 * time.Second)
	ticker := time.NewTicker(1 * time.Second)
	// always be suer to stop the ticker
	defer ticker.Stop()

	done := make(chan bool)

	go func() {
		for {
			select {
			case <-ticker.C:
				fmt.Println("Tick!")
			case <-done:
				return
			}
		}
	}()

	<-timer.C
	fmt.Println("It's time!")
	close(done)
}
