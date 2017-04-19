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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SlackApplication {
  @RequestMapping("/")
  public String home() {
    return "Hello World";
  }

  @RequestMapping(value = "/hello", produces = "application/json")
  public Message hello(
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

    Message message = new Message();
    message.setText("Hello " + userName);
    message.setResponseType(Message.ResponseType.IN_CHANNEL);
    return message;
  }

  /**
   * <a
   * href="https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
   * App Engine health checking</a> requires responding with 200 to {@code /_ah/health}.
   */
  @RequestMapping("/_ah/health")
  public String healthy() {
    // Message body required though ignored
    return "Still surviving.";
  }

  public static void main(String[] args) {
    SpringApplication.run(SlackApplication.class, args);
  }
}
