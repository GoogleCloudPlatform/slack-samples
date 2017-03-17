# Running a slash command for Slack on Google Cloud Platform with Java

This tutorial demonstrates how to build and deploy a [slash command for
Slack](https://api.slack.com/slash-commands) on [Google Cloud Platform](https://cloud.google.com/).

Slash commands provide a way to call external web services from a [Slack](https://slack.com/)
conversation. For example, the [Giphy
app](https://get.slack.help/hc/en-us/articles/204714258-Add-Giphy-search-to-Slack) can be run by
**/giphy** in a conversation.

1\.  [Objectives](#objectives)  
2\.  [Costs](#costs)  
3\.  [Before you begin](#beforeyoubegin)  
4\.  [Getting the sample code](#gettingthesamplecode)  
5\.  [Deploying to App Engine](#deployingtoappengine)  
6\.  [Create a Slack app](#createaslackapp)  
7\.  [Adding a slash command custom integration](#addingaslashcommandcustomintegration)  

<a name="objectives"></a>

## 1\. Objectives

- Deploy a Java application to the [App Engine flexible environment][flexible].
- Create a [slash command for Slack](https://api.slack.com/slash-commands).
- Load tokens from the [Google Cloud Runtime Config
  API](https://cloud.google.com/deployment-manager/runtime-configurator/) 

[flexible]: https://cloud.google.com/appengine/docs/flexible/java/

<a name="costs"></a>

## 2\. Costs

This tutorial uses billable components of Cloud Platform, including:

- Google App Engine Flexible Environment

Use the [Pricing Calculator][pricing] to generate a cost estimate based on your projected usage.

[pricing]: https://cloud.google.com/products/calculator

<a name="beforeyoubegin"></a>

## 3\. Before you begin

1.  Follow the [quickstart for Java in the App Engine flexible
    environment](https://cloud.google.com/appengine/docs/flexible/java/quickstart) to 
    set up your environment to deploy the sample applications App Engine.
    1.  Download and install the [Google Cloud SDK](https://cloud.google.com/sdk/docs/).
    1.  [Install and configure Apache Maven](http://maven.apache.org/index.html).
    1.  [Create a new Google Cloud Platform project, or use an existing
        one](https://console.cloud.google.com/project).
    1.  [Enable billing for your
        project](https://support.google.com/cloud/answer/6293499#enable-billing).
    1. Initialize the Cloud SDK.

           gcloud init

1.  Create a [new Slack team](https://slack.com/), or use an team where you have
    permissions to add custom integrations.

<a name="gettingthesamplecode"></a>

## 4\. Getting the sample code

Get the latest sample code from GitHub using Git or download the repository as a ZIP file.
([Download](https://github.com/GoogleCloudPlatform/slack-samples/archive/master.zip))

    git clone https://github.com/GoogleCloudPlatform/slack-samples.git


The `java/command/1-start` directory contains a simple Hello World application, which you will
modify to support Slack slash commands.

    cd java/command/1-start

<a name="deployingtoappengine"></a>

## 5\. Deploying to App Engine

To run the application locally, use the Maven Jetty plugin.

    mvn clean jetty:run-exploded

View the app at [localhost:8080](http://localhost:8080).

To deploy the app to App Engine, run

    mvn clean appengine:deploy

After the deploy finishes (can take up to 10 minutes), you can view your application at
`https://YOUR_PROJECT.appspot.com`, where `YOUR_PROJECT` is your Google Cloud project ID. You can
see the new version deployed on the [App Engine section of the Google Cloud
Console](https://console.cloud.google.com/appengine/versions).

For a more detailed walkthrough, see the [getting started
guide for Java in the App Engine flexible
environment](https://cloud.google.com/java/getting-started/hello-world).


<a name="createaslackapp"></a>

## 6\. Create a Slack app

Create a new Slack app by going to the [app management
page](https://api.slack.com/apps) and clicking the **Create new app** button.

1.  Give the app a name, such as "Hello World".
1.  Choose the Slack team where you want it installed.

<a name="addingaslashcommandcustomintegration"></a>

## 7\. Adding a slash command custom integration

When someone types a [slash command in
Slack](https://api.slack.com/slash-commands#how_do_commands_work), the Slack servers send an HTTP
request to your application.

1.  Select the [Slash commands](https://api.slack.com/slash-commands) feature
    in the **Add features and functionality** section.
1.  Click the **Create new command** button.
1.  Set the command name to `/greet`.
1.  Set the request URL to `https://YOUR_PROJECT.appspot.com/send-greeting`,
    replacing `YOUR_PROJECT` with your Google Cloud Project ID.
1.  Set the short description to "Sends a greeting".
1.  Click the **Save** button at the bottom of the page to save your settings.

Reinstall the app to ensure the slash command is installed.

1.  Select the **Install app** item in the left navigation under **Settings**.
1.  Click the **Install app** or **Reinstall app** button.
    
When you run the slash command, Slack will send a request to your app and show the result.

-   Type `/greet` in your Slack team.

You should see the text `Hello, world.` in response.
