package fanin

import (
	"testing"
	"time"
)

func TestFunnel(t *testing.T) {
	sources := make([]<-chan int, 0)

	const (
		sourceCnt = 3
		dataCnt   = 5
	)
	for i := 0; i < sourceCnt; i++ {
		ch := make(chan int)
		sources = append(sources, ch)

		go func() {
			defer close(ch)

			for i := 1; i <= dataCnt; i++ {
				ch <- i
				time.Sleep(time.Millisecond * 100)
			}
		}()
	}

	dest := Funnel(sources...)
	var cnt = 0
	for d := range dest {
		cnt++
		t.Log(d)
	}
	if cnt != sourceCnt*dataCnt {
		t.Error("invalid result")
	}
}
