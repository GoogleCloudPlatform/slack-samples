/* *****************************************************************************
Copyright 2016 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
********************************************************************************

This is a sample Slack bot built with Botkit.

# RUN THE BOT:

  Get a Bot token from Slack:

    -> http://my.slack.com/services/new/bot

  Run your bot from the command line:

    echo my-slack-token > slack-token
    slack_token_path=./slack-token node demo_bot.js

  See README.md for how to run the bot on Kubernetes.

# USE THE BOT:

  Find your bot inside Slack to send it a direct message.

  Say: "Hello"

  The bot will reply "Hello!"

# EXTEND THE BOT:

  Botkit is has many features for building cool and useful bots!

  Read all about it here:

    -> http://howdy.ai/botkit
*/

var Botkit = require('botkit');
var fs = require('fs');

var controller = Botkit.slackbot({debug: false});

if (!process.env.slack_token_path) {
  console.log('Error: Specify slack_token_path in environment');
  process.exit(1);
}

fs.readFile(process.env.slack_token_path, (err, data) => {
  if (err) {
    console.log('Error: Specify token in slack_token_path file');
    process.exit(1);
  }
  data = String(data);
  data = data.replace(/\s/g, "");
  controller.spawn({token: data}).startRTM(function(err) {
    if (err) {
      throw new Error(err);
    }
  });
});

controller.hears(
    ['hello', 'hi'], ['direct_message', 'direct_mention', 'mention'],
    function(bot, message) { bot.reply(message, "Hello."); });
