package app

import (
	"errors"
	"io"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"spike.com/key-value-store/kvs"
)

type RestServer struct {
	kvs.KVS
}

func NewRestServer(service kvs.KVS) (RestServer, error) {
	return RestServer{service}, nil
}

func (rest *RestServer) Start(addr string) {
	r := mux.NewRouter()

	r.HandleFunc("/", rest.helloMuxHandler)
	r.HandleFunc("/v1/{key}", rest.keyValuePutHandler).Methods("PUT")
	r.HandleFunc("/v1/{key}", rest.keyValueGetHandler).Methods("GET")
	r.HandleFunc("/v1/{key}", rest.keyValueDeleteHandler).Methods("DELETE")

	log.Fatal(http.ListenAndServe(addr, r))
}

func (rest *RestServer) helloMuxHandler(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("Hello gorilla/mux!\n"))
}

func (rest *RestServer) keyValuePutHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	value, err := io.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// put to store
	err = rest.Put(key, string(value))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// logging
	rest.WritePut(key, string(value))
	select {
	case err = <-rest.Err():
		http.Error(w, err.Error(), http.StatusInternalServerError)
	default:
		w.WriteHeader(http.StatusCreated)
	}
}

func (rest *RestServer) keyValueGetHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	value, err := rest.Get(key)
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

func (rest *RestServer) keyValueDeleteHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	// delete in store
	err := rest.Delete(key)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// logging
	rest.WriteDelete(key)
	select {
	case err := <-rest.Err():
		http.Error(w, err.Error(), http.StatusInternalServerError)
	default:
		w.WriteHeader(http.StatusOK)
	}
}
