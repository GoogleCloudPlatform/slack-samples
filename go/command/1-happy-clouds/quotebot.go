// Copyright 2016 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Package quotebot demonstrates how to create an App Engine application as a
// Slack slash command.
package quotebot

import (
	"encoding/json"
	"html/template"
	"math/rand"
	"net/http"

	"google.golang.org/appengine"
	"google.golang.org/appengine/log"
)

type slashResponse struct {
	ResponseType string `json:"response_type"`
	Text         string `json:"text"`
}

var indexTmpl = template.Must(template.ParseFiles("index.html"))

func init() {
	http.HandleFunc("/", handleIndex)
	http.HandleFunc("/quotes/random", handleRandomQuote)
}

func handleIndex(w http.ResponseWriter, r *http.Request) {
	if err := indexTmpl.Execute(w, nil); err != nil {
		c := appengine.NewContext(r)
		log.Errorf(c, "Error executing indexTmpl template: %s", err)
	}
}

// handleRandomQuote returns a random quote as a Slack slash command.
//
// If the token parameter doesn't match the secret token provided by Slack, we
// reject the request.  To let the app know what this token is, when you create
// the custom integration, populate the token variable in config.go.
//
// Once we have verified the request is valid, we send back a random quote from
// the quotes slice, created in config.go.
func handleRandomQuote(w http.ResponseWriter, r *http.Request) {
	if token != "" && r.PostFormValue("token") != token {
		http.Error(w, "Invalid Slack token.", http.StatusBadRequest)
		return
	}
	c := appengine.NewContext(r)
	log.Errorf(c, "Got token: %s", r.PostFormValue("token"))

	w.Header().Set("content-type", "application/json")

	resp := &slashResponse{
		ResponseType: "in_channel",
		Text:         quotes[rand.Intn(len(quotes))],
	}
	if err := json.NewEncoder(w).Encode(resp); err != nil {
		log.Errorf(c, "Error encoding JSON: %s", err)
		http.Error(w, "Error encoding JSON.", http.StatusInternalServerError)
		return
	}
}
