# Running a Bot for Slack on Google Cloud Platform with Java

This tutorial demonstrates how to build and deploy a [bot for
Slack](https://api.slack.com/bot-users) on [Google Cloud Platform](https://cloud.google.com/).

## Objectives

- Deploy a Java application to the [App Engine flexible environment][flexible].
- Create a [bot for Slack](https://api.slack.com/bot-users).
- Load tokens from the [Google Cloud Runtime Config
  API](https://cloud.google.com/deployment-manager/runtime-configurator/)

[flexible]: https://cloud.google.com/appengine/docs/flexible/java/

## Before you begin

Follow the [slash command tutorial](../command) to set up a Slack app and Google
Cloud project.

## Costs

This tutorial uses billable components of Cloud Platform, including:

- Google App Engine Flexible Environment

Use the [Pricing Calculator][cloud-pricing] to generate a cost estimate based on
your projected usage.

Slack is free for up to 10 apps and integrations. Check the [Slack pricing
page][slack-pricing] for details.

[cloud-pricing]: https://cloud.google.com/products/calculator
[slack-pricing]: https://slack.com/pricing

## Getting the sample code

Get the latest sample code from GitHub using Git or download the repository as a ZIP file.
([Download](https://github.com/GoogleCloudPlatform/slack-samples/archive/master.zip))

    git clone https://github.com/GoogleCloudPlatform/slack-samples.git

The `java/events/1-start` directory contains a simple Hello World bot.

    cd java/events/1-start

## Configuring the Slack app

You will be adding a bot user to the app you created [slash command tutorial](../command).

1.  Selecting your Slack app in the [app management page](https://api.slack.com/apps).
1.  Click the **Bot users** feature in the navigation on the left-hand side.
1.  Click the **Add a bot user** button.
1.  Switch **Always show my bot as online** to **On**.
1.  Add the bot user.

### Install the command to your team

To add the bot user to your team, you need to reinstall the app.

1.  Go to the **OAuth and permissions** feature using the left-hand navigation.
1.  Click the **Install app to team** button.

## 6\. Deploying your app

This sample uses the [RuntimeConfig
API configuration](https://cloud.google.com/deployment-manager/runtime-configurator/) to store configuration
values, such as secret tokens.

<a name="createaconfiguration"></a>

### 6.1\. Create a configuration

Using the command-line [Google Cloud SDK](https://cloud.google.com/sdk/), create a new runtime
configuration.

    gcloud beta runtime-config configs create slack-samples-java

<a name="copytheverificationtoken"></a>

### 6.2\. Copy the verification token

To ensure that HTTP requests to your app originate from Slack, Slack provides a validation token.
You check that the token field of an incoming request matches the expected value.

1.  Select your app on the [app management page](https://api.slack.com/apps).
1.  Go to the **Basic information** page.
1.  Scroll to **App credentials** and copy the **Verification token** text.

<a name="addverificationtokentotheconfiguration"></a>

### 6.3\. Add verification token to the configuration

Create a variable called `slack-token` in the runtime configuration. Use the Cloud SDK from the
command-line to add the variable.

    gcloud beta runtime-config configs variables set \
        slack-token "YOUR-TOKEN-VALUE" \
        --is-text --config-name slack-samples-java

Replace `YOUR-TOKEN-VALUE` with the verification token value you copied from the Slack app
management page.

<a name="[optional]runninglocally"></a>

### 6.4\. [Optional] Running locally

To run the application locally, use the Maven Spring Boot plugin.

    mvn clean spring-boot:run

View the app at http://localhost:8080.

Since Slack requires a public URL to send webhooks, you may wish to [use a service like ngrok to
test your Slack application locally](https://api.slack.com/tutorials/tunneling-with-ngrok).

<a name="deployingtoappengine"></a>

### 6.5\. Deploying to App Engine

To deploy the app to App Engine, run

    mvn clean appengine:deploy

After the deploy finishes (can take up to 10 minutes), you can view your application at
https://YOUR_PROJECT.appspot.com, where YOUR_PROJECT is your Google Cloud project ID. You can see
the new version deployed on the App Engine section of the Google Cloud Console.

For a more detailed walkthrough, see the getting started guide for Java in the App Engine flexible
environment.

<a name="tryingtheslashcommand"></a>

## Enabling the events API

1.  Select your app on the [app management page](https://api.slack.com/apps).
1.  Click the **Event subscriptions** feature in the left-hand navigation.
1.  Switch **Enable events** to **On**.
1.  Enter `https://YOUR_PROJECT.appspot.com/hello` as your request URL, replacing `YOUR_PROJECT`
    with your Google Cloud project ID.
1.  Enter a short description, such as "Sends a greeting."

<a name="installthecommandtoyourteam"></a>

## 7\. Trying the slash command

When you run the slash command, Slack will send a request to your app and show the result.

-   Type `/greet` in your Slack team.

You should see the text `Hello, world.` in response.

## TODO: pub/sub

    gcloud beta pubsub topics create slack-events
    gcloud beta pubsub subscriptions create slack-events-hello --topic slack-events

    openssl rand -base64 18
    gcloud beta runtime-config configs variables set pubsub-token "$(openssl rand -base64 18)" --is-text --config-name slack-samples-java
    gcloud beta runtime-config configs variables get-value pubsub-token --config-name slack-samples-java
    gcloud beta pubsub subscriptions create slack-events-hello-push --topic slack-events \
        --push-endpoint "https://swast-scratch.appspot.com/pubsub/push?token=$(gcloud beta runtime-config configs variables get-value pubsub-token --config-name slack-samples-java)" \
        --ack-deadline 10
