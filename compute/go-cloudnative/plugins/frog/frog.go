package main

type frog struct{}

func (d frog) Says() string {
	return "ribbit"
}

// exported symbol
var Animal frog
