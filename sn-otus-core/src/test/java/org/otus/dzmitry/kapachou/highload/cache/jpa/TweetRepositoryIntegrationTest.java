package org.otus.dzmitry.kapachou.highload.cache.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.otus.dzmitry.kapachou.highload.cache.SocialNetworkCoreIntegrationTest;
import org.otus.dzmitry.kapachou.highload.cache.model.Tweet;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.otus.dzmitry.kapachou.highload.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TweetRepositoryIntegrationTest extends SocialNetworkCoreIntegrationTest {

    @Autowired
    PersonService personService;
    @Autowired
    TweetService tweetService;

    @Test
    public void verifyTweetCorrectlySavedWithAuthor() {
        var person = personService.get(11200031L);
        setupPersonAuthentication(person);

        var tweets = person.getTweets();
        Assert.assertEquals(0, tweets.size());

        tweetService.saveAll(Set.of(
                buildTweet("this is my first tweet"),
                buildTweet("this is my second tweet")
        ));

        var current = personService.get(11200031L);
        Assert.assertFalse(current.getTweets().isEmpty());
        Assert.assertEquals(2, current.getTweets().size());

        var friend = personService.get(11100011L);
        setupPersonAuthentication(friend);
        var friendsTweets = tweetService.findTweetsIncludingFriendsTweets(person);
        Assert.assertFalse(friend.getTweets().isEmpty());
        Assert.assertEquals(3, friend.getTweets().size());

        Assert.assertFalse(friend.getFriends().isEmpty());
        Assert.assertTrue(friend.getTweets().size() < friendsTweets.size());
        Assert.assertEquals(5, friendsTweets.size());

        Map<String, List<Tweet>> authorTweetsMap = friendsTweets.stream()
                .collect(Collectors.groupingBy(Tweet::getAuthor));

        Assert.assertEquals(2, authorTweetsMap.entrySet().size());
        Assert.assertTrue(authorTweetsMap
                .containsKey(String.format("%s %s", person.getFirstname(), person.getLastname())));
        Assert.assertEquals(2, authorTweetsMap
                .get(String.format("%s %s", person.getFirstname(), person.getLastname())).size());
        Assert.assertTrue(authorTweetsMap
                .containsKey(String.format("%s %s", friend.getFirstname(), friend.getLastname())));
        Assert.assertEquals(3, authorTweetsMap
                .get(String.format("%s %s", friend.getFirstname(), friend.getLastname())).size());

    }

    private static Tweet buildTweet(String text) {
        var t = new Tweet();
        t.setText(text);
        return t;
    }


}
