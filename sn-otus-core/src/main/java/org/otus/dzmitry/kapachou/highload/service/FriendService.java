package org.otus.dzmitry.kapachou.highload.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;
import org.otus.dzmitry.kapachou.highload.model.Friend;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendService extends PersonService {

    final AuthenticationCachedPersonService authenticationService;

    public FriendService(PersonRepository personRepository, AuthenticationCachedPersonService authenticationService) {
        super(personRepository);
        this.authenticationService = authenticationService;
    }

    public void addAsFriend(Long friendId) {
        Person current = authenticationService.getAuthenticatedPerson();
        Person friend = get(friendId);
        // adding each other
        current.getFriends().add(new Friend(current, friend));
        friend.getFriends().add(new Friend(friend, current));

        getRepository().saveAll(Set.of(friend, current));
    }

    public void discardFriend(Long friendId) {
        Person current = authenticationService.getAuthenticatedPerson();
        Person friend = get(friendId);
        // discarding friends' relationships
        current.getFriends().remove(new Friend(current, friend));
        friend.getFriends().remove(new Friend(friend, current));

        getRepository().saveAll(Set.of(friend, current));
    }

    public Collection<String> getFriends() {
        return authenticationService.getAuthenticatedPerson()
                .getFriends().stream()
                .map(Friend::getFriend)
                .collect(Collectors.toList());
    }

}
