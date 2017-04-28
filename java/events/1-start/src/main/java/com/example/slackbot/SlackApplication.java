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

import com.example.slackbot.events.Event;
import com.example.slackbot.events.EventCallback;
import com.example.slackbot.events.EventRequest;
import com.example.slackbot.events.MessageEvent;
import com.example.slackbot.events.UrlVerification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
@RestController
public class SlackApplication {

  private static final Logger log = LoggerFactory.getLogger(SlackApplication.class);

  @RequestMapping("/")
  public String handleHome() {
    return "Hello World";
  }

  @RequestMapping(value = "/event", produces = "application/json")
  public Object handleEvent(@RequestBody EventRequest request) throws IOException {
    // Verify that this request is from Slack.
    String desiredToken = System.getenv("SLACK_TOKEN");
    if (!desiredToken.equals(request.getToken())) {
      throw new AccessDeniedException("forbidden");
    }

    if (request instanceof UrlVerification) {
      UrlVerification verification = (UrlVerification) request;

      // Respond with the challenge to verify that we got this request.
      UrlVerificationResponse response = new UrlVerificationResponse();
      response.setChallenge(verification.getChallenge());
      return response;
    } else if (request instanceof EventCallback) {
      handleEventCallback((EventCallback) request);
      return "ok";
    }
    // Accept the request, but doing nothing, if we don't know what it was.
    return "ok";
  }

  private void handleEventCallback(EventCallback callback) throws IOException {
    Event event = callback.getEvent();
    if (!(event instanceof MessageEvent)) {
      return;
    }

    MessageEvent message = (MessageEvent) event;
    String text = message.getText();
    if (text == null) {
      return;
    }

    // The bot OAuth access token is needed to make REST calls to Slack.
    String botToken = System.getenv("SLACK_BOT_TOKEN");

    // Say hello if you see your name.
    String botId = getBotId(botToken);
    if (text.contains(String.format("<@%s", botId))) {
      sayHello(botToken, message.getChannel());
    }
  }

  /**
   * Checks for errors in the response.
   */
  private void checkResponse(SlackResponse response) throws IOException {
    if (response.getWarning() != null && !response.getWarning().isEmpty()) {
      log.warn(response.getWarning());
    }
    if (response.getError() != null && !response.getError().isEmpty()) {
      log.warn(response.getError());
    }
    if (!response.isOk()) {
      throw new IOException();
    }

  }

  private String getBotId(String accessToken) throws IOException {
    RestTemplate restTemplate = new RestTemplate();
    AuthTestResponse response =
        restTemplate.getForObject(
            "https://slack.com/api/auth.test?token={token}", AuthTestResponse.class, accessToken);
    checkResponse(response);

    return response.getUserId();
  }

  private void sayHello(String accessToken, String channel) throws IOException {
    RestTemplate restTemplate = new RestTemplate();
    SlackResponse response =
        restTemplate.getForObject(
            "https://slack.com/api/chat.postMessage?token={token}&channel={channel}&text={text}",
            SlackResponse.class,
            accessToken,
            channel,
            "Hello!");
    checkResponse(response);
  }

  /**
   * <a
   * href="https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
   * App Engine health checking</a> requires responding with 200 to {@code /_ah/health}.
   */
  @RequestMapping("/_ah/health")
  public String handleHealthCheck() {
    // Message body required though ignored
    return "Still surviving.";
  }

  public static void main(String[] args) {
    SpringApplication.run(SlackApplication.class, args);
  }
}
