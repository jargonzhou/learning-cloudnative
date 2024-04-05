package main

type duck struct{}

func (d duck) Says() string {
	return "quack"
}

// exported symbol
var Animal duck
