# Hosting a Slack Slash Command on Google App Engine

This sample shows you how to build a [Slack internal
integration](https://api.slack.com/internal-integrations) with a [slash
command](https://api.slack.com/slash-commands) running on [Google App
Engine][go-appengine].

This tutorial uses the [Go programming language][golang]. See the [Google Cloud Platform
Slack Samples repository](https://github.com/GoogleCloudPlatform/slack-samples)
for a list of samples in other languages.

[go-appengine]: https://cloud.google.com/appengine/docs/go/
[golang]: https://golang.org/

## Objectives

- Deploy a Slack slash command server to App Engine.
- Configure an internal Slack app to call an App Engine application for slash
  commands.

## Costs

This tutorial uses billable components of Cloud Platform including Google App
Engine standard environment. Use the [Pricing
Calculator](https://cloud.google.com/products/calculator/#id=6d866c0e-b928-4786-b2ab-bed5c380a2fd)
to estimate the costs for your usage. Note that a portion of your usage is
covered by the [App Engine free
tier](https://cloud.google.com/appengine/quotas#Instances).

Slack is free for up to 10 apps and integrations. Check the [Slack pricing
page](https://slack.com/pricing) for details.

## Before you begin

1.  [Install and configure Go](https://golang.org/doc/install).
1.  Set up [App Engine and your development
    environment](https://cloud.google.com/appengine/docs/standard/go/quickstart).
1.  Create a [new Slack app](https://api.slack.com/tutorials/slack-apps-hello-world).

## Getting the sample code

1.  Get the latest sample code.

        go get -u github.com/GoogleCloudPlatform/slack-samples/go/command/1-happy-clouds

1.  Change to the sample code directory.

        cd $GOPATH/src/github.com/GoogleCloudPlatform/slack-samples/go/command/1-happy-clouds

## Setup Slack

Configure a new [slash command](https://api.slack.com/slash-commands) for your
Slack app.

1.  Go to the Slack app that you created from [your apps page](https://api.slack.com/apps).
1.  Create a new slash command.
    1.  Click **Slash commands** under the **Features** header on the left-hand
        side of the Slack app configuration page.
    1.  Click the **Create new command** button.
    1.  Enter the command name, like `/happycloud`.
    1.  For the **Request URL**, enter
        `https://YOUR-PROJECT.appspot.com/quotes/random`, replace
        `YOUR-PROJECT` with your [Google Cloud Project
        ID](https://support.google.com/cloud/answer/6158840?hl=en).
    1.  For the description, enter "Displays a happy quote".
    1.  Click the **Save** button in the lower right-hand corner.
1.  Copy the verification token.
    1.  Click **Basic information** under the **Settings** header on the
        left-hand navigation.
    1.  Copy the **Verification token** to your clipboard.

[Be careful](https://api.slack.com/docs/oauth-safety) with your token. Treat it
like you would any other secret token. Do not store tokens in version control
or share them publicly.

## Configure the Go app

1.  Open `config.go` in a text editor.
1.  Set the token field to the value you copied. Change the line

        token string

    to

        token = "YOUR-TOKEN-VALUE"

## Build and Deploy

1.  Deploy the app to App Engine.

        goapp deploy -application your-project app.yaml

    Replace `your-project` with your Google Cloud Project ID.
1.  If this is not the first App Engine application you have deployed to this
    project, go to the [Google Cloud Platform
    Console](https://console.cloud.google.com/appengine/versions), select
    version 1 in the App Engine versions and click **Migrate traffic** to send
    requests to the deployed version.

## Try It Out

In your Slack team, write a message with the text `/happycloud`. You should see
a happy little quote in response.

## Next steps

- Try [part 2](../2-oauth) to add your app to multiple Slack teams.
- Explore the other [Slack APIs](https://api.slack.com/).
- Check out the [other Slack samples for Google Cloud
  Platform](https://github.com/GoogleCloudPlatform/slack-samples)
