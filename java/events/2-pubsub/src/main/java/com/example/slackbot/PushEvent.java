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

/**
 * A model of a Pub/Sub push event.
 *
 * <p>See: https://cloud.google.com/pubsub/docs/push
 */
public class PushEvent {
  private PushMessage message;
  private String subscription;

  public PushMessage getMessage() {
    return message;
  }

  public void setMessage(PushMessage message) {
    this.message = message;
  }

  public String getSubscription() {
    return subscription;
  }

  public void setSubscription(String subscription) {
    this.subscription = subscription;
  }
}
