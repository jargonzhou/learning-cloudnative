package main

import (
	"context"
	"log"
	"math/rand"
	"strconv"
	"time"

	dapr "github.com/dapr/go-sdk/client"
)

// code
func main() {
	BINDING_NAME := "checkout"
	BINDING_OPERATION := "create"
	for i := 0; i < 10; i++ {
		time.Sleep(5000)
		orderId := rand.Intn(1000-1) + 1
		client, err := dapr.NewClient()
		if err != nil {
			panic(err)
		}
		defer client.Close()
		ctx := context.Background()
		//Using Dapr SDK to invoke output binding
		in := &dapr.InvokeBindingRequest{Name: BINDING_NAME, Operation: BINDING_OPERATION, Data: []byte(strconv.Itoa(orderId))}
		err = client.InvokeOutputBinding(ctx, in)
		if err != nil {
			log.Println(err)
		}
		log.Println("Sending message: " + strconv.Itoa(orderId))
	}
}
