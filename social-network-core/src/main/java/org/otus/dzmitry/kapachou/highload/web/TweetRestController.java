package org.otus.dzmitry.kapachou.highload.web;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.Tweet;
import org.otus.dzmitry.kapachou.highload.service.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/tweets")
@AllArgsConstructor
public class TweetRestController {

    final TweetService tweetService;

    @GetMapping("/{id}")
    public Tweet getTweet(@PathVariable Long id) {
        return tweetService.get(id);
    }

    @GetMapping
    public Collection<Tweet> getTweets() {
        return tweetService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tweet createTweet(@RequestBody Tweet tweet) {
        return tweetService.save(tweet);
    }

    @DeleteMapping
    public void removeTweet(@PathVariable Long tweetId) {
        tweetService.delete(tweetId);
    }
}
