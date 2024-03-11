package org.otus.dzmitry.kapachou.highload.service;

import com.google.common.collect.EvictingQueue;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.Tweet;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Objects;
import java.util.Queue;

@Service
@AllArgsConstructor
public class TweetTimelineFeedService {

    private final IMap<Long, Collection<Tweet>> tweetsCache;

    private final PersonService personService;
    private final TweetService tweetService;

    @PostConstruct
    private void preComputeTimelineOnStartForRecentSessions() {
        personService.getRecentPersonSessions()
                .forEach(person -> {
                    Queue<Tweet> cachedTweets = EvictingQueue.create(1_000);
                    cachedTweets.addAll(tweetService.findTweetsIncludingFriendsTweets(person));
                    tweetsCache.putAsync(person.getId(), cachedTweets);
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
        }
    }

    private void updateFriendsCacheTimeline(Tweet tweet) {
        var author = tweet.getAuthorAsPerson();
        author.getFriends().forEach(f -> {
            var friend = f.getFriendAsPerson();
            if (tweetsCache.containsKey(friend.getId())) {
                var friendFeed = tweetsCache.get(friend.getId());
                friendFeed.add(tweet);
                tweetsCache.putAsync(friend.getId(), friendFeed);
            }
        });
        var feed = tweetsCache.get(author.getId());
        feed.add(tweet);
        tweetsCache.put(author.getId(), feed);
    }

}
