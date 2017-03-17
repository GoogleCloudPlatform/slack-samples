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
// Slack app, with an attached slash command.
package quotebot

import (
	"encoding/json"
	"html/template"
	"math/rand"
	"net/http"

	"github.com/gorilla/sessions"
	"github.com/satori/go.uuid"
	"golang.org/x/oauth2"
	"google.golang.org/appengine"
	"google.golang.org/appengine/log"
)

type slackResponse struct {
	OK    bool   `json:"ok"`
	Error string `json:"error"`
}

type slashResponse struct {
	ResponseType string `json:"response_type"`
	Text         string `json:"text"`
}

var (
	sessionStore *sessions.CookieStore
	indexTmpl    = template.Must(template.ParseFiles("index.html"))
	oauthTmpl    = template.Must(template.ParseFiles("oauth.html"))
)

func init() {
	// Configure storage method for session-wide information.
	sessionStore = sessions.NewCookieStore([]byte(sessionSecret))
	sessionStore.Options = &sessions.Options{
		HttpOnly: true,
	}

	http.HandleFunc("/", handleIndex)
	http.HandleFunc("/oauth2callback", handleOAuthCallback)
	http.HandleFunc("/quotes/random", handleRandomQuote)
}

type indexPage struct {
	OAuthURL string
}

func handleIndex(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	if oauthConfig.ClientID == "" || oauthConfig.ClientSecret == "" {
		http.Error(w, "Need to configure OAuth in config.go.", http.StatusInternalServerError)
		return
	}

	sessionID := uuid.NewV4().String()
	oauthFlowSession, err := sessionStore.New(r, sessionID)
	if err != nil {
		log.Errorf(c, "Error creating OAuth session: %s", err)
		http.Error(w, "Error creating OAuth session.", http.StatusInternalServerError)
		return
	}
	oauthFlowSession.Options.MaxAge = 10 * 60 // 10 minutes
	if err := oauthFlowSession.Save(r, w); err != nil {
		log.Errorf(c, "Error saving OAuth session: %s", err)
		http.Error(w, "Error saving OAuth session.", http.StatusInternalServerError)
		return
	}

	// Use the session ID for the "state" parameter.
	// This protects against CSRF (cross-site request forgery).
	// See https://godoc.org/golang.org/x/oauth2#Config.AuthCodeURL for more detail.
	oauthURL := oauthConfig.AuthCodeURL(sessionID, oauth2.ApprovalForce, oauth2.AccessTypeOnline)
	if err := indexTmpl.Execute(w, &indexPage{OAuthURL: oauthURL}); err != nil {
		log.Errorf(c, "Error executing indexTmpl template: %s", err)
	}
}

type oauthPage struct {
	Message string
}

// handleOAuthCallback accepts requests from adding the application to a Slack team.
//
// To make this work: populate the oauthConfig in config.go after you register
// your app in Slack at https://api.slack.com/applications/new.
func handleOAuthCallback(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	if r.URL.Query().Get("error") == "access_denied" {
		if err := oauthTmpl.Execute(w, &oauthPage{"App installation cancelled."}); err != nil {
			log.Errorf(c, "Error executing oauthTmpl template: %s", err)
		}
		return
	}

	if _, err := sessionStore.Get(r, r.FormValue("state")); err != nil {
		log.Errorf(c, "invalid state parameter: %s", err)
		http.Error(w, "Invalid state parameter. Try 'Add to Slack' again.", http.StatusBadRequest)
		return
	}
	code := r.FormValue("code")

	// We do not need the OAuth token to handle Slack commands, so we just
	// discard it.
	//
	// Best practice would be to save this to a database so that you can
	// associate the Slack user with your app's user and also ask for
	// additional permissions in the future.
	if _, err := oauthConfig.Exchange(c, code); err != nil {
		log.Errorf(c, "Error authorizing against Slack: %s", err)
		http.Error(w, "Unexpected error authorizing against Slack.", http.StatusInternalServerError)
		return
	}

	if err := oauthTmpl.Execute(w, &oauthPage{"Welcome! You can now run the slash command."}); err != nil {
		log.Errorf(c, "Error executing oauthTmpl template: %s", err)
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

	w.Header().Set("content-type", "application/json")

	resp := &slashResponse{
		ResponseType: "in_channel",
		Text:         quotes[rand.Intn(len(quotes))],
	}
	if err := json.NewEncoder(w).Encode(resp); err != nil {
		c := appengine.NewContext(r)
		log.Errorf(c, "Error encoding JSON: %s", err)
		http.Error(w, "Error encoding JSON.", http.StatusInternalServerError)
		return
	}
}
