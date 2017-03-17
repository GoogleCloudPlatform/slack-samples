# Hosting a Slack App on Google App Engine

This sample builds on [part 1 of the slash command
tutorial](../1-happy-clouds). I recommend starting there before deploying this
sample.

## Getting the sample code

1.  Get the latest sample code.

        go get -u github.com/GoogleCloudPlatform/slack-samples/go/command/2-oauth

1.  Change to the sample code directory.

        cd $GOPATH/src/github.com/GoogleCloudPlatform/slack-samples/go/command/2-oauth

## Setup tokens

1.  Configure the app you created in [part 1](../1-happy-clouds) by selecting
    it from the [app management page](api.slack.com/apps).
1.  From the **Basic information** page, copy the **Verification token** and
    write it to `config.go`, as you did in part 1.
1.  Copy the **Client ID** and write it to `config.go`.
1.  Click the **Show** button for the **Client secret**. Copy the secret value
    and write it to `config.go`.

[Be careful](https://api.slack.com/docs/oauth-safety) with these tokens. Treat
them like you would any other secret token. Do not store tokens in version
control or share them publicly.

## Setup OAuth

1.  Click **OAuth & permissions** item under the features heading on the
    left navigation.
1.  Add a new redirect URL. Set it to 
    `https://YOUR-PROJECT.appspot.com/oauth2callback`, replacing `YOUR-PROJECT`
    with your Google Cloud Project ID.
1.  Click the **Save URLs** button.

## Build and Deploy

1.  Deploy the app to App Engine.

        goapp deploy -application your-project app.yaml

    Replace `your-project` with your Google Cloud Project ID.
1.  If this is not the first App Engine application you have deployed to this
    project, go to the [Google Cloud Platform
    Console](https://console.cloud.google.com/appengine/versions), select
    version 1 in the App Engine versions and click **Migrate traffic** to send
    requests to the deployed version.

## Test the app

In your Slack team, write a message with the text `/happycloud`. You should see
a happy little quote in response.

## Activate distribution

1.  Go to the Slack [app management page](api.slack.com/apps) for your app.
1.  Click **Manage distribution** on the left navigation under the settings
    header.
1.  Verify you've completed all the steps in the **Share your apps with other
    teams** section.
1.  Click the **Activate public distribution** button.

## Add the app to a second Slack team

Now that distribution is activated, use the [Slack
button](https://api.slack.com/docs/slack-button) to share your Slack app with
other teams. This lets you test out the app on multiple teams before you submit
your app to the [Slack app directory](https://slack.com/apps).

1.  Open your App Engine application by going to the URL
    `https://YOUR-PROJECT.appspot.com`, replacing `YOUR-PROJECT` with your
    Google Cloud Project ID.
1.  Click the **Add to Slack** button.
1.  Select a second Slack team to add your app to.
1.  After the app is added in your second Slack team, write a message with the
    text `/happycloud`. You should see a happy little quote in response.

## Next steps

- Learn more about [using OAuth 2.0 with
  Slack](https://api.slack.com/docs/oauth).
- Explore the other [Slack APIs](https://api.slack.com/).
- Check out the [other Slack samples for Google Cloud
  Platform](https://github.com/GoogleCloudPlatform/slack-samples)
