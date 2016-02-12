# Hosting a Slack App on Google App Engine

This sample builds on the [slash command custom integration
tutorial](../1-custom-integration). I recommend starting there before deploying
this sample.  Slack also [recommends][slack-apps-recommend] developing as a
custom integration before converting into an app.

[slack-apps-recommend]: https://api.slack.com/slack-apps#what-it-does

## Setup Slack

You must register a [new app][slack-app-new].

- Set the callback in the registration form to
  https://YOUR-PROJECT.appspot.com/oauth2callback, replace YOUR-PROJECT with
  your Google Cloud Project ID.
- Write the Client ID and Client Secret it gives you to `config.go`.
- Attach a new slash command to the app. Point it at
  `https://YOUR-PROJECT.appspot.com/quotes/random`, replace YOUR-PROJECT with
  your Google Cloud Project ID. Give it a name like `/happycloudapp`.
- Write the token it gives you to `config.go`.

[slack-app-new]: https://api.slack.com/applications/new

## Build and Deploy

Then, deploy. Replace your-project with your Google Cloud Project ID:

```
goapp deploy -application your-project app.yaml
```


## Try It Out

Run `/happycloudapp` in Slack and see a nice quote!

