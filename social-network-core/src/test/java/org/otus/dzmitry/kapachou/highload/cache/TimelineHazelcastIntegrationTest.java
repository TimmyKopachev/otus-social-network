package org.otus.dzmitry.kapachou.highload.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.otus.dzmitry.kapachou.highload.SocialNetworkCoreIntegrationTest;
import org.otus.dzmitry.kapachou.highload.model.Tweet;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.otus.dzmitry.kapachou.highload.service.SessionService;
import org.otus.dzmitry.kapachou.highload.service.TweetTimelineFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.IntStream;

@ExtendWith(SpringExtension.class)
public class TimelineHazelcastIntegrationTest extends SocialNetworkCoreIntegrationTest {
    @Autowired
    private HazelcastInstance hazelcastInstance;
    @Autowired
    private IMap<Long, Collection<Tweet>> tweetsCache;
    @Autowired
    private PersonService personService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private TweetTimelineFeedService timelineFeedService;

    @Test
    public void verifyHazelcastCluster() {
        Assert.assertEquals(1, hazelcastInstance.getCluster().getMembers().size());
    }

    @Test
    public void verifyCacheIngestTweetAndUpdateFriendsTimeLine() {
        var recentDate = Instant.now().minus(7, ChronoUnit.DAYS);

        var person = personService.get(11331003L);
        Assert.assertNotNull(person);

        setupPersonAuthentication(person);

        var session = sessionService.getSessionByPersonId(person.getId());
        Assert.assertNotNull(session);

        Assert.assertTrue(tweetsCache.containsKey(person.getId()));
        Assert.assertTrue(session.getLastActivityAt().isAfter(recentDate));
        var personTweetTimeline = timelineFeedService.fetchTweetsFeed();
        Assert.assertEquals(3, personTweetTimeline.size());

        var friend = personService.get(11100011L);
        Assert.assertNotNull(person);

        setupPersonAuthentication(friend);
        var friendTweetTimeline = timelineFeedService.fetchTweetsFeed();
        Assert.assertEquals(3, friendTweetTimeline.size());

        timelineFeedService.postTweet(writeMockTweet());
        Assert.assertEquals(4, tweetsCache.get(friend.getId()).size());
        Assert.assertEquals(4, tweetsCache.get(person.getId()).size());
    }

    @Test
    public void verifyTimelineTweetCapacityInCacheLimitedBy1000() {
        var person = personService.get(11331003L);
        Assert.assertNotNull(person);

        setupPersonAuthentication(person);

        IntStream.rangeClosed(0, 1_050)
                .mapToObj(i -> writeMockTweet())
                .forEach(timelineFeedService::postTweet);

        var personTweetTimeline = timelineFeedService.fetchTweetsFeed();
        Assert.assertFalse(personTweetTimeline.isEmpty());
        Assert.assertEquals(1_000, personTweetTimeline.size());
    }

    private static Tweet writeMockTweet() {
        Tweet t = new Tweet();
        t.setText("""
                Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lectus mauris ultrices eros in cursus turpis massa. In fermentum et sollicitudin ac orci.
                Faucibus ornare suspendisse sed nisi lacus sed. Vivamus at augue eget arcu dictum varius.
                Lobortis scelerisque fermentum dui faucibus in ornare quam.
                """);
        return t;
    }

}
