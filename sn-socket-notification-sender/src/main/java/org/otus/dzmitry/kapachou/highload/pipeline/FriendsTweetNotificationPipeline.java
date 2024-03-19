package org.otus.dzmitry.kapachou.highload.pipeline;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.otus.dzmitry.kapachou.highload.cache.channel.TweetFriendsNotificationRequest;
import org.otus.dzmitry.kapachou.highload.cache.pipeline.StreamPipeline;
import org.otus.dzmitry.kapachou.highload.cache.serialization.CommonStreamConfig;
import org.otus.dzmitry.kapachou.highload.pipeline.properties.FriendsTweetNotificationPipelineProperties;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.Properties;

@Slf4j
@AllArgsConstructor
public class FriendsTweetNotificationPipeline implements StreamPipeline {

    final FriendsTweetNotificationPipelineProperties notificationPipelineProperties;
    final SimpMessageSendingOperations messageSocketTemplate;

    @Override
    public Properties initProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, notificationPipelineProperties.getApplicationId());
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, notificationPipelineProperties.getBootstrapServer());
        return properties;
    }

    @Override
    public Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        streamsBuilder.stream(
                        notificationPipelineProperties.getInputTopic(),
                        Consumed.with(
                                CommonStreamConfig.getSerde(Long.class),
                                CommonStreamConfig.getSerde(TweetFriendsNotificationRequest.class)))
                .foreach((tweetId, notificationRequest) -> {
                    messageSocketTemplate.convertAndSendToUser(notificationRequest.getFriendPrincipalReceiver(),
                            "/queue/tweet-feed-updates", notificationRequest.getTweet());
                });

        return streamsBuilder.build();
    }
}
