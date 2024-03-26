package org.otus.dzmitry.kapachou.highload.service;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AbstractCrudService;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;
import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.Tweet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TweetService extends AbstractCrudService<Tweet> {

    private final AuthenticationCachedPersonService authenticationService;
    private final TweetRepository tweetRepository;
    private final PersonService personService;

    public Collection<Tweet> findTweetsIncludingFriendsTweets(Person person) {
        return tweetRepository.findTweetsWithFriendsTweetsByPersonId(person.getId());
    }

    @Override
    @Transactional
    public Tweet save(Tweet tweet) {
        tweet.setAuthor(authenticationService.getAuthenticatedPerson());
        return super.save(tweet);
    }

    @Override
    @Transactional
    public Iterable<Tweet> saveAll(Collection<Tweet> data) {
        var author = authenticationService.getAuthenticatedPerson();
        data.forEach(t -> t.setAuthor(author));
        return super.saveAll(data);
    }

    @Override
    protected BaseRepository<Tweet> getRepository() {
        return tweetRepository;
    }
}
