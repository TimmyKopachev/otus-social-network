package org.otus.dzmitry.kapachou.highload.pipeline;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.otus.dzmitry.kapachou.highload.channel.TweetFriendsNotificationRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class TweetFriendsRequestNotificationPipeline {

    final KafkaTemplate<Long, TweetFriendsNotificationRequest> tweetSocketNotificationRequestKafkaTemplate;

    public void requestTweetSocketNotification(TweetFriendsNotificationRequest request) {
        tweetSocketNotificationRequestKafkaTemplate
                .send("TWEED-FRIENDS-SUBMIT-NOTIFICATION_TOPIC", request.getTweet().getTweetID(), request)
                .exceptionally(
                        exception -> {
                            log.error("Issue occurred during request of socket-tweet notification:{} due to <{}>",
                                    request.getTweet().getTweetID(), exception.getMessage());
                            return null;
                        })
                .thenApply(result -> !Objects.nonNull(result));
    }
}
