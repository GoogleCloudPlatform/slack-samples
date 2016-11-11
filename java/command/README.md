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
5\.  [Before you begin](#beforeyoubegin-1)  
6\.  [Adding a slash command custom integration](#addingaslashcommandcustomintegration)  

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

<a name="beforeyoubegin-1"></a>

## 5\. Before you begin

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

<a name="addingaslashcommandcustomintegration"></a>

## 6\. Adding a slash command custom integration

When someone types a [slash command in
Slack](https://api.slack.com/slash-commands#how_do_commands_work), the Slack servers send an HTTP
request to your application.

-   Add a slash command custom integration to your Slack team from the [custom integrations
    management page](https://slack.com/apps/manage/custom-integrations).
-   Use `/greet` as the command name.
-   Set the URL on the integration settings page to the URL for your deployed application (for
    example: `https://YOUR_PROJECT.appspot.com/send-greeting`).
-   Click the Save button at the bottom of the page to save your settings.
    
When you run the slash command, Slack will send a request to your app and show the result.

-   Type `/greet` in your Slack team.

You should see the text `Hello, world.` in response.
