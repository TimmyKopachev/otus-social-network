package org.otus.dzmitry.kapachou.highload.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.otus.dzmitry.kapachou.highload.pipeline.FriendsTweetNotificationPipeline;
import org.otus.dzmitry.kapachou.highload.pipeline.properties.FriendsTweetNotificationPipelineProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Slf4j
@Configuration
@EnableConfigurationProperties(
        value = {
                FriendsTweetNotificationPipelineProperties.class
        })
@AllArgsConstructor
public class KStreamPipelineConfiguration {

    final FriendsTweetNotificationPipelineProperties notificationPipelineProperties;
    final SimpMessagingTemplate webSocket;

    @Bean
    FriendsTweetNotificationPipeline friendsTweetNotificationPipeline() {
        return new FriendsTweetNotificationPipeline(notificationPipelineProperties, webSocket);
    }

}
