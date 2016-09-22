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
