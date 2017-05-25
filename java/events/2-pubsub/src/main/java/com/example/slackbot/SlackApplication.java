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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.spi.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootApplication
@RestController
public class SlackApplication {
  private static final String PUBSUB_TOPIC = "slack-events";

  private static final Logger log = LoggerFactory.getLogger(SlackApplication.class);

  private Publisher publisher;

  public static void main(String[] args) {
    SpringApplication.run(SlackApplication.class, args);
  }

  @RequestMapping("/")
  public String handleHome() {
    return "Hello World";
  }

  /**
   * Handles a Slack slash command request.
   *
   * <p>See: https://github.com/GoogleCloudPlatform/slack-samples/tree/master/java/command
   */
  @RequestMapping(value = "/hello", produces = "application/json")
  public SlackMessage hello(
      @ModelAttribute("token") String token,
      @ModelAttribute("team_id") String teamId,
      @ModelAttribute("team_domain") String teamDomain,
      @ModelAttribute("channel_id") String channelId,
      @ModelAttribute("channel_name") String channelName,
      @ModelAttribute("user_id") String userId,
      @ModelAttribute("user_name") String userName,
      @ModelAttribute("command") String command,
      @ModelAttribute("text") String text,
      @ModelAttribute("response_url") String responseUrl) {
    String desiredToken = System.getenv("SLACK_TOKEN");
    if (!desiredToken.equals(token)) {
      throw new AccessDeniedException("forbidden");
    }

    SlackMessage message = new SlackMessage();
    message.setText("Hello " + userName);
    message.setResponseType(SlackMessage.ResponseType.IN_CHANNEL);
    return message;
  }

  /**
   * Handles a Slack event subscription.
   *
   * <p>See: https://github.com/GoogleCloudPlatform/slack-samples/tree/master/java/events
   */

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

  @RequestMapping(value = "/pubsub/push")
  public String handlePubSubPush(
      @RequestParam("token") String token, @RequestBody PushEvent pubSubEvent) throws IOException {
    // Verify that this request is from Pub/Sub.
    String desiredToken = System.getenv("PUBSUB_TOKEN");
    if (!desiredToken.equals(token)) {
      throw new AccessDeniedException("forbidden");
    }

    if (pubSubEvent.getMessage() == null || pubSubEvent.getMessage().getData() == null) {
      // Acknowledge the event, but don't process further.
      return "ok";
    }

    // Parse the message.
    String data = pubSubEvent.getMessage().getData();
    byte[] dataBytes = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
    ObjectMapper mapper = new ObjectMapper();
    Event event = mapper.readValue(dataBytes, Event.class);
    if (!(event instanceof MessageEvent)) {
      return "ok";
    }
    MessageEvent message = (MessageEvent) event;

    // The bot OAuth access token is needed to make REST calls to Slack.
    String botToken = System.getenv("SLACK_BOT_TOKEN");

    //// Say hello if you see your name.
    String botId = getBotId(botToken);
    if (message.getText().contains(String.format("<@%s", botId))) {
      sayHello(botToken, message.getChannel());
    }
    return "ok";
  }

  /**
   * <a
   * href="https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
   * App Engine health checking</a> requires responding with 200 to {@code /_ah/health}.
   */
  @RequestMapping("/_ah/health")
  public String handleHealthCheck() {
    // SlackMessage body required though ignored
    return "Still surviving.";
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

    // Create a publisher on the topic if it's not already created.
    Publisher publisher = this.publisher;
    if (publisher == null) {
      publisher = Publisher.defaultBuilder(
          TopicName.create(ServiceOptions.getDefaultProjectId(), PUBSUB_TOPIC))
          .build();
    }

    // construct a pubsub message from the payload
    ObjectMapper mapper = new ObjectMapper();
    String payload = mapper.writeValueAsString(message);
    PubsubMessage pubsubMessage =
        PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(payload)).build();

    publisher.publish(pubsubMessage);
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
}
