# Slack Notifications for SSH logins

This example will walk you through how to get a [Slack](https://slack.com)
notification when someone SSHs into a [Google Compute
Engine](https://cloud.google.com/compute/) instance.


## Get a Slack hook

Create a [Slack incoming webhook](https://api.slack.com/incoming-webhooks) and
save the webhook URL, like
`https://hooks.slack.com/services/YOUR/SLACK/INCOMING-WEBHOOK`.


## Set up the PAM hook.

Running,

    ./install.sh https://hooks.slack.com/services/YOUR/SLACK/INCOMING-WEBHOOK

creates a `/etc/slack` directory and copies the `login-notify.sh` script there.
It then configures `/etc/pam.d/sshd` to run the script whenever someone SSHes
into the machine.


## Disclaimer

This is not an official Google product.

