package org.otus.dzmitry.kapachou.highload.service;

import com.google.common.collect.EvictingQueue;
import com.hazelcast.map.IMap;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.cache.channel.TweetDetails;
import org.otus.dzmitry.kapachou.highload.cache.channel.TweetFriendsNotificationRequest;
import org.otus.dzmitry.kapachou.highload.cache.model.Friend;
import org.otus.dzmitry.kapachou.highload.cache.model.Person;
import org.otus.dzmitry.kapachou.highload.cache.model.Tweet;
import org.otus.dzmitry.kapachou.highload.pipeline.TweetFriendsRequestNotificationPipeline;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
public class TweetTimelineFeedService {

    private final IMap<Long, Collection<Tweet>> tweetsCache;
    private final TweetFriendsRequestNotificationPipeline tweetNotificationPipeline;

    private final PersonService personService;
    private final TweetService tweetService;

    @PostConstruct
    private void preComputeTimelineOnStartForRecentSessions() {
        Executors.newSingleThreadExecutor().execute(() -> {
            personService.getActivePersonSessions()
                    .forEach(person -> {
                        Queue<Tweet> cachedTweets = EvictingQueue.create(1_000);
                        cachedTweets.addAll(tweetService.findTweetsIncludingFriendsTweets(person));
                        tweetsCache.putAsync(person.getId(), cachedTweets);
                    });
        });
    }

    private boolean isPersonTimelineCached(Person person) {
        return tweetsCache.containsKey(person.getId());
    }

    public Collection<Tweet> fetchTweetsFeed() {
        var current = personService.getCurrentAuthenticatedUser();
        if (isPersonTimelineCached(current)) {
            return tweetsCache.get(current.getId());
        }
        Queue<Tweet> cachedTweets = EvictingQueue.create(1_000);
        cachedTweets.addAll(tweetService.findTweetsIncludingFriendsTweets(current));
        tweetsCache.put(current.getId(), cachedTweets);
        return cachedTweets;
    }

    public void postTweet(Tweet tweet) {
        Tweet saved = tweetService.save(tweet);
        if (Objects.nonNull(saved)) {
            updateFriendsCacheTimeline(saved);
            // pushing to socket service to notify friends for a new tweet coming up
            saved.getAuthorAsPerson().getFriends().stream()
                    .map(Friend::getFriendAsPerson)
                    .map(Person::getUsername)
                    .map(fun -> buildNotificationRequest(saved, fun))
                    .forEach(tweetNotificationPipeline::requestTweetSocketNotification);
        }
    }

    private static TweetFriendsNotificationRequest buildNotificationRequest(Tweet tweet, String receiver) {
        var request = new TweetFriendsNotificationRequest();
        var tweetDetails = new TweetDetails();
        tweetDetails.setTweetID(tweet.getId());
        tweetDetails.setAuthor(tweet.getAuthor());
        tweetDetails.setCreatedAt(tweet.getCreatedAt());
        tweetDetails.setText(tweet.getText());
        request.setFriendPrincipalReceiver(receiver);
        request.setTweet(tweetDetails);
        return request;
    }

    private void updateFriendsCacheTimeline(Tweet tweet) {
        var author = tweet.getAuthorAsPerson();
        CompletableFuture.runAsync(() ->
                author.getFriends().forEach(f -> {
                    var friend = f.getFriendAsPerson();
                    if (tweetsCache.containsKey(friend.getId())) {
                        var friendFeed = tweetsCache.get(friend.getId());
                        friendFeed.add(tweet);
                        tweetsCache.putAsync(friend.getId(), friendFeed);
                    }
                }));
        var feed = tweetsCache.get(author.getId());
        feed.add(tweet);
        tweetsCache.put(author.getId(), feed);
    }

}
