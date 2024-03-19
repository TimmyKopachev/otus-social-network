package org.otus.dzmitry.kapachou.highload.cache;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class HzCacheTweetCapacityIntegrationTest extends TimelineHazelcastIntegrationTest {

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
}
