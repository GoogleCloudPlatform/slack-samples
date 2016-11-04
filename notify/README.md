# Sending connection notifications to Slack

This tutorial demonstrates how to send a [Slack](https://slack.com)
notification when someone SSHs into a [Google Compute
Engine](https://cloud.google.com/compute/) instance.

## Objectives

- Send a request to a Slack incoming webhook
- Add a PAM hook to run a script on SSH logins

## Before you begin

1. Create a Linux Google Compute Engine (GCE) instance. You can follow [the GCE
   Linux quickstart
   guide](https://cloud.google.com/compute/docs/quickstart-linux) to create one.
2. Create a [new Slack team](https://slack.com/), or use an team where you have
   permissions to add custom integrations.

## Costs

This tutorial uses billable components of Cloud Platform including Google
Compute Engine. Use the [Pricing
Calculator](https://cloud.google.com/products/calculator/#id=6d866c0e-b928-4786-b2ab-bed5c380a2fd)
to estimate the costs for your usage.

Slack is free for up to 10 apps and integrations. Check the [Slack pricing
page](https://slack.com/pricing) for details.

## Connect to your instance

[Connect to your Google Compute Engine
instance](https://cloud.google.com/compute/docs/instances/connecting-to-instance).
The easiest way to do this is to use the [SSH button from Google Cloud
Console](https://console.cloud.google.com/compute/instances).

Optionally, you can clone the sample code repository and change to the `notify`
directory.

```shell
git clone https://github.com/GoogleCloudPlatform/slack-samples.git
cd slack-samples/notify
```

## Creating a Slack incoming webhook

Create a [Slack incoming webhook](https://api.slack.com/incoming-webhooks) from
the [custom integrations page for your Slack
team](https://slack.com/apps/manage/custom-integrations). This will give you a
webhook URL, like
`https://hooks.slack.com/services/YOUR/SLACK/INCOMING-WEBHOOK`.

Write this webhook URL to a file called `slack-hook` using your favorite text
editor. This tutorial uses `nano`.

```shell
nano slack-hook
# paste in URL, write out, and exit
```

## Creating and testing the notification script.

Create a [script called
`login-notify.sh`](https://github.com/GoogleCloudPlatform/slack-samples/blob/master/notify/login-notify.sh)
to post a message to your Slack webhook.

```shell
#!/usr/bin/env bash
script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [[ $PAM_TYPE != "close_session" ]] ; then
  host=$(hostname)

  # Include the username and remote IP address in the message.
  message="SSH Login: ${PAM_USER} from ${PAM_RHOST} on ${host}"

  # Read the webhook URL from the slack-hook file.
  hook=$(cat "${script_dir}/slack-hook")

  # Send a POST HTTP request to the Slack webhook.
  curl -X POST --data-urlencode "payload={\"text\": \"${message}\"}" "${hook}"
fi
```

Test the script by setting the `PAM_USER` and `PAM_RHOST` variables and running
the script.

```shell
chmod +x login-notify.sh
PAM_USER=$USER PAM_RHOST=testhost ./login-notify.sh
```

The script sends a POST request to your Slack webhook. You should receive a
Slack message notifying you of this.

## Adding the PAM hook.

A PAM hook can run a script to run whenever someone SSHes into the machine.
Verify that SSH is using PAM by making sure there is a line "`UsePAM yes`" in
the `/etc/ssh/sshd_config` file.

```shell
sudo nano /etc/ssh/sshd_config
```

You can now set up the PAM hook.

The [`install.sh`
script](https://github.com/GoogleCloudPlatform/slack-samples/blob/master/notify/install.sh)
creates a `/etc/slack` directory and copies the `login-notify.sh` script
and `slack-hook` configuration there.

```shell
sudo mkdir -p /etc/slack
sudo cp login-notify.sh slack-hook /etc/slack
```

The script then configures `/etc/pam.d/sshd` to run the script whenever someone
SSHes into the machine.

```shell
sudo echo "# Notify via Slack that someone has logged in." >> /etc/pam.d/sshd
sudo echo "session    optional     pam_exec.so seteuid /etc/slack/login-notify.sh" >> /etc/pam.d/sshd
```

Keep this SSH window open in case something went wrong and verify that you can
login from another SSH terminal. You should receive another notification on
Slack, indicating that you just connected.

## Cleaning up

To prevent unnecessary charges, clean up the resources created for this
tutorial.

1. [Delete any compute engine
   instances](https://cloud.google.com/compute/docs/instances/stopping-or-deleting-an-instance).
2. Remove the custom integration from Slack.

## Next steps

- Explore the other [Slack APIs](https://api.slack.com/).
- Check out the [other Slack samples for Google Cloud
  Platform](https://github.com/GoogleCloudPlatform/slack-samples)

## Disclaimer

This is not an official Google product.
