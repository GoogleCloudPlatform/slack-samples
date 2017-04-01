/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.slackbot;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A model of a Slack slash command request.
 *
 * <p>See: https://api.slack.com/slash-commands
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SlashCommand {
  private String token;
  private String teamId;
  private String teamDomain;
  private String channelId;
  private String channelName;
  private String userId;
  private String userName;
  private String command;
  private String text;
  private String responseUrl;

  public String getToken() {
    return token;
  }

  public void setToken(String value) {
    this.token = value;
  }

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String value) {
    this.teamId = value;
  }

  public String getTeamDomain() {
    return teamDomain;
  }

  public void setTeamDomain(String value) {
    this.teamDomain = value;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String value) {
    this.channelId = value;
  }

  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String value) {
    this.channelName = value;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String value) {
    this.userId = value;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String value) {
    this.userName = value;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String value) {
    this.command = value;
  }

  public String getText() {
    return text;
  }

  public void setText(String value) {
    this.text = value;
  }

  public String getResponseUrl() {
    return responseUrl;
  }

  public void setResponseUrl(String value) {
    this.responseUrl = value;
  }
}
