package main

import (
    "fmt"
    "log"
    "encoding/json"
    "net/http"
)

type HelloMessage struct {
    Message string `json:"message"`
}

func helloHandler(w http.ResponseWriter, req *http.Request) {
    var message = &HelloMessage{Message: "Hello! from GoLang"}
    response, err := json.Marshal(message)
    if err != nil {
        fmt.Println(err)
        return
    }

 	w.Header().Set("content-type", "application/json")

    fmt.Fprintf(w, string(response))
}


func main() {
    http.HandleFunc("/hello", helloHandler)

    fmt.Printf("Starting server at port 8090\n")
    if err := http.ListenAndServe(":8090", nil); err != nil {
        log.Fatal(err)
    }
}