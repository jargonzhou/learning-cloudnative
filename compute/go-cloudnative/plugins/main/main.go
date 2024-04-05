package main

import (
	"fmt"
	"log"
	"os"
	"plugin"
)

type Sayer interface {
	Says() string
}

func main() {
	if len(os.Args) != 2 {
		log.Fatal("usage: run main/main.go <animal>")
	}
	name := os.Args[1]
	module := fmt.Sprintf("./%s/%s.so", name, name)

	// open plugin
	p, err := plugin.Open(module)
	if err != nil {
		log.Fatal(err)
	}
	// lookup symbol
	symbol, err := p.Lookup("Animal")
	if err != nil {
		log.Fatal(err)
	}

	animal, ok := symbol.(Sayer)
	if !ok {
		log.Fatal("it's not a Sayer")
	}

	fmt.Printf("A %s says: %q.\n", name, animal.Says())
}
