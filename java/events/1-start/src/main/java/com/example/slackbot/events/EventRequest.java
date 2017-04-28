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

package com.example.slackbot.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A model of requests sent to a Slack event endpoint.
 *
 * <p>See: https://api.slack.com/events/api
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    defaultImpl = EventRequest.class)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = UrlVerification.class, name = "url_verification"),
        @JsonSubTypes.Type(value = EventCallback.class, name = "event_callback")
    })
public class EventRequest {
  private String type;
  private String token;

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
}
