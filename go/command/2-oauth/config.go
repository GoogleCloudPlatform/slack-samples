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

package quotebot

import (
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/slack"
)

var (
	// This configuration is needed to turn this into a distributable Slack
	// app.
	//
	// You must register the app at https://api.slack.com/applications/new.
	// Set the callback in the registration form to
	// https://YOUR-SERVER.appspot.com/oauth2callback.
	oauthConfig = &oauth2.Config{
		// TODO: Set ClientID to value from App registration page.
		ClientID: "",
		// TODO: Set ClientSecret to value from App registration page.
		ClientSecret: "",
		Scopes:       []string{"commands"},
		Endpoint:     slack.Endpoint,
	}

	// TODO: Update "something-very-secret" with a hard to guess string or byte sequence.
	sessionSecret = "something-very-secret"

	// TODO: Set the token variable. It is needed to verify that the
	// requests to the slash command come from Slack.
	token string

	// Quotes from Bob Ross. https://youtu.be/YLO7tCdBVrA
	quotes = []string{
		"Every day is a good day when you paint.",
		"Let's build a happy little cloud.",
		"Let's build some happy little trees.",
	}
)
