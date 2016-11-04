#!/usr/bin/env bash
# Copyright 2016 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

if [ ! -f ./slack-hook ] ; then
  echo "The file ./slack-hook must exist and contain the URL to your incoming"
  echo "Slack webook.  Example:"
  echo
  echo "$ cat ./slack-hook"
  echo "https://hooks.slack.com/services/YOUR/SLACK/INCOMING-WEBHOOK"
  exit 1
fi

echo "Copying scripts."
mkdir -p /etc/slack
cp login-notify.sh slack-hook /etc/slack

echo "Configuring /etc/pam.d/sshd."
echo "# Notify via Slack that someone has logged in." >> /etc/pam.d/sshd
echo "session    optional     pam_exec.so seteuid /etc/slack/login-notify.sh" >> /etc/pam.d/sshd
