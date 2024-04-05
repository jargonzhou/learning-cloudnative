package fanout

import (
	"sync"
	"testing"
)

func TestSplit(t *testing.T) {
	source := make(chan int)
	const (
		destCnt = 5
		dataCnt = 10
	)

	dests := Split(source, destCnt)

	go func() {
		for i := 1; i <= dataCnt; i++ {
			source <- i
		}

		close(source)
	}()

	wg := sync.WaitGroup{}
	wg.Add(len(dests))

	var totalCnt = 0
	var m sync.Mutex
	for i, ch := range dests {
		go func(i int, d <-chan int) {
			defer wg.Done()

			for v := range d {
				m.Lock()
				totalCnt++
				m.Unlock()
				t.Logf("%d got %d\n", i, v)
			}
		}(i, ch)
	}

	wg.Wait()
	if totalCnt != dataCnt {
		t.Error("invalid result")
	}
}
