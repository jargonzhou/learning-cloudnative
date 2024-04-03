package app

import (
	"errors"
	"io"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"spike.com/key-value-store/kvs"
)

func helloMuxHandler(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("Hello gorilla/mux!\n"))
}

func keyValuePutHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	value, err := io.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// put to store
	err = store.Put(key, string(value))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// logging
	(*logger).WritePut(key, string(value))
	select {
	case err = <-(*logger).Err():
		http.Error(w, err.Error(), http.StatusInternalServerError)
	default:
		w.WriteHeader(http.StatusCreated)
	}
}

func keyValueGetHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	value, err := store.Get(key)
	if errors.Is(err, kvs.ErrorNoSuchKey) {
		http.Error(w, err.Error(), http.StatusNotFound)
		return
	}
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Write([]byte(value))
}

func keyValueDeleteHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	// delete in store
	err := store.Delete(key)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// logging
	(*logger).WriteDelete(key)
	select {
	case err := <-(*logger).Err():
		http.Error(w, err.Error(), http.StatusInternalServerError)
	default:
		w.WriteHeader(http.StatusOK)
	}
}

var store *kvs.MemoryKVStore
var logger *kvs.TransactionLogger

func startRestServer(addr string, s *kvs.MemoryKVStore, l *kvs.TransactionLogger) {
	r := mux.NewRouter()
	store = s
	logger = l

	r.HandleFunc("/", helloMuxHandler)
	r.HandleFunc("/v1/{key}", keyValuePutHandler).Methods("PUT")
	r.HandleFunc("/v1/{key}", keyValueGetHandler).Methods("GET")
	r.HandleFunc("/v1/{key}", keyValueDeleteHandler).Methods("DELETE")

	log.Fatal(http.ListenAndServe(addr, r))
}
