# Hosting a Botkit Slack Bot with Kubernetes

This example will walk you through running a
[Botkit](https://github.com/howdyai/botkit) Slack Bot on Kubernetes.


## Setting up your environment

Set up the [prerequisites](http://kubernetes.io/gettingstarted/). We will be
using [Google Container Registry](https://cloud.google.com/container-registry/)
to store our Docker image and
[Google Container Engine](https://cloud.google.com/container-engine/), a hosted
version of Kubernetes, to run them.

To start,
[create a Kubernetes cluster](https://cloud.google.com/container-engine/docs/clusters/operations#creating_a_container_cluster),
if you don't already have one. To do this on Google Container Engine, run:

```bash
gcloud container clusters create slackbot
```


## Get a Slack token

Create a [Slack bot user](https://api.slack.com/bot-users) and get an
authentication token.


## Upload token to Kubernetes

We will be loading this token in our bot using
[secrets](http://kubernetes.io/v1.1/docs/user-guide/secrets.html).

```bash
./generate-secret.sh MY-SLACK-TOKEN
kubectl create -f slack-token-secret.yaml
```


## Build the container

First, build the Docker container. Replace my-cloud-project-id below with your
Google Cloud Project ID. This tags the container so that gcloud can upload it to
your private Google Container Registry.

```bash
export PROJECT_ID=my-cloud-project-id
docker build -t gcr.io/${PROJECT_ID}/slack-bot .
```

Once the build completes, upload it.

```bash
gcloud docker push gcr.io/${PROJECT_ID}/slack-bot
```


## Running the container

First, create a replication controller configuration, populated with your Google
Cloud Project ID, so that Kubernetes knows where to load the Docker image from.

```bash
./generate-rc.sh $PROJECT_ID
```

Now, tell Kubernetes to create the replication controller to start running the
bot.

```bash
kubectl create -f slack-bot-rc.yaml
```

You can check the status of your bot with:

```bash
kubectl get pods
```

Now your bot should be online and respond to "Hello".


## Shutting down

To shutdown your bot, we tell Kubernetes to delete the replication controller.

```bash
kubectl delete -f slack-bot-rc.yaml
```


## Cleanup

If you have created a container cluster, you may still get charged for the
Google Compute Engine resources it is using, even if they are idle. To delete
the cluster, run:

```bash
gcloud container clusters delete slackbot
```

This deletes the Google Compute Engine instances that are running the cluster.


## Disclaimer

This is not an official Google product.
