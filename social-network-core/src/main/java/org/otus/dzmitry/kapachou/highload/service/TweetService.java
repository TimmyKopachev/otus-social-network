package org.otus.dzmitry.kapachou.highload.service;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AbstractCrudService;
import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Tweet;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@AllArgsConstructor
public class TweetService extends AbstractCrudService<Tweet> {

    final TweetRepository tweetRepository;
    final PersonService personService;

    @Override
    public Collection<Tweet> findAll() {
        var authPerson = personService.getCurrentAuthenticatedUser();
        return tweetRepository.findTweetsWithFriendsTweetsByPersonId(authPerson.getId());
    }

    @Override
    @Transactional
    public Tweet save(Tweet tweet) {
        tweet.setAuthor(personService.getCurrentAuthenticatedUser());
        return super.save(tweet);
    }

    @Override
    protected BaseRepository<Tweet> getRepository() {
        return tweetRepository;
    }
}
