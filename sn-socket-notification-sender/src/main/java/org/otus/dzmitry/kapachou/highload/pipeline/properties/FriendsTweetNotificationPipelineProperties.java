package org.otus.dzmitry.kapachou.highload.pipeline.properties;

import org.otus.dzmitry.kapachou.highload.pipeline.StreamPipelineProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pipeline.social.tweet-feed-notificator")
public class FriendsTweetNotificationPipelineProperties extends StreamPipelineProperties {

}
