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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A model of a Slack message response.
 *
 * <p>See: https://api.slack.com/docs/messages
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SlackMessage {
  private String text;
  private ResponseType responseType;

  public void setText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public void setResponseType(ResponseType responseType) {
    this.responseType = responseType;
  }

  public ResponseType getResponseType() {
    return responseType;
  }

  public enum ResponseType {
    @JsonProperty("ephemeral")
    EPHEMERAL,

    @JsonProperty("in_channel")
    IN_CHANNEL;
  }
}
