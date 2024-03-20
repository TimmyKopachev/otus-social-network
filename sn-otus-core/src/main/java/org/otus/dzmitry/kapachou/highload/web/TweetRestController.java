package org.otus.dzmitry.kapachou.highload.web;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.Tweet;
import org.otus.dzmitry.kapachou.highload.service.TweetTimelineFeedService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/tweets")
@AllArgsConstructor
public class TweetRestController {

    final TweetTimelineFeedService feedService;


    @GetMapping
    public Collection<Tweet> getTweets() {
        return feedService.fetchTweetsFeed();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTweet(@RequestBody Tweet tweet) {
        feedService.postTweet(tweet);
    }

}
