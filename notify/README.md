# Slack Notifications for SSH logins

This example will walk you through how to get a [Slack](https://slack.com)
notification when someone SSHs into a [Google Compute
Engine](https://cloud.google.com/compute/) instance.

This tutorial assumes you have created a Linux Google Compute Engine instance.
You can follow [this
guide](https://cloud.google.com/compute/docs/linux-quickstart) to create one
and follow along.


## Get a Slack hook

Create a [Slack incoming webhook](https://api.slack.com/incoming-webhooks) and
save the webhook URL, like
`https://hooks.slack.com/services/YOUR/SLACK/INCOMING-WEBHOOK`.


## Test the script.

Create a file slack-hook with the webhook URL and test the webhook out.

    nano slack-hook
    # paste in URL, write out, and exit
    PAM_USER=$USER PAM_RHOST=testhost ./login-notify.sh

The script sends a POST request to your Slack webhook. You should receive a
Slack message notifying you of this.


## Set up the PAM hook.

We will be adding a PAM hook to run whenever someone SSHes into the machine.
Verify that SSH is using PAM by making sure there is a line "`UsePAM yes`" in the
`/etc/ssh/sshd_config` file.

    sudo nano /etc/ssh/sshd_config

We can now set up the PAM hook.  Running,

    ./install.sh

creates a `/etc/slack` directory and copies the `login-notify.sh` script and
`slack-hook` configuration there.  It then configures `/etc/pam.d/sshd` to run
the script whenever someone SSHes into the machine.

Keep this SSH window open in case something went wrong and verify that you can
login from another SSH terminal. You should receive another notification on
Slack, this time with the real remote host IP address.


## Disclaimer

This is not an official Google product.

