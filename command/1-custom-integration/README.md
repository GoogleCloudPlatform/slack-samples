# Hosting a Slack Slash Command on Google App Engine

This sample shows you how to build a slash command custom integration for Slack,
running on App Engine.

## Prerequisites

You need to be able to deploy Go applications to App Engine. I recommend
following the [guide for Go App Engine][go-appengine], first. At a minimum, you
should [install Go][go-install] and the [Go App Engine SDK][go-appengine-sdk].

[go-appengine]: https://cloud.google.com/appengine/docs/go/
[go-install]: https://golang.org/doc/install
[go-appengine-sdk]: https://cloud.google.com/appengine/downloads#Google_App_Engine_SDK_for_Go

## Setup Slack

We need to create a [new slash command][new-slash-command] custom integration.

- Point it at `https://YOUR-PROJECT.appspot.com/quotes/random`, replace
  YOUR-PROJECT with your Google Cloud Project ID.
- Give it a name, like `/happycloud`.
- Write the token it gives you to `config.go`.

[new-slash-command]: https://my.slack.com/services/new/slash-commands

## Build and Deploy

Then, deploy. Replace your-project with your Google Cloud Project ID:

```
goapp deploy -application your-project app.yaml
```


## Try It Out

Run `/happycloud` in Slack and see a nice quote!

