# Running a slash command for Slack on Google Cloud Platform - Step 1

This is the solution directory for step one in the tutorial for running a [slash command for
Slack](https://api.slack.com/slash-commands) on [Google Cloud Platform](https://cloud.google.com/).

This tutorial uses [App Engine flexible environment][flexible] for more details on running an app
on App Engine, follow the [quickstart tutorial][flexible-quickstart].

[flexible]: https://cloud.google.com/appengine/docs/flexible/java/
[flexible-quickstart]: https://cloud.google.com/appengine/docs/flexible/java/quickstart

## Running locally

    $ mvn jetty:run

## Deploying

    $ mvn appengine:deploy
