package org.otus.dzmitry.kapachou.highload.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.otus.dzmitry.kapachou.highload.SocialNetworkCoreIntegrationTest;
import org.otus.dzmitry.kapachou.highload.model.Friend;
import org.otus.dzmitry.kapachou.highload.service.FriendService;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class FriendRepositoryIntegrationTest extends SocialNetworkCoreIntegrationTest {

    @Autowired
    PersonService personService;
    @Autowired
    FriendService friendService;

    @Test
    public void verifyPersonHasFriendsAndCanAddNewFriend() {
        var person = personService.get(11331003L);
        Assert.assertNotNull(person);

        setupPersonAuthentication(person);

        var friends = friendService.getFriends();
        Assert.assertFalse(friends.isEmpty());
        Assert.assertEquals(friends.size(), 2);
        Assert.assertTrue(friends.containsAll(Set.of("dzmitry kapachou", "elena kapachova")));

        // adding new friend
        friendService.addAsFriend(11200031L);
        var updatedFriends = friendService.getFriends();
        Assert.assertEquals(updatedFriends.size(), 3);
        Assert.assertTrue(updatedFriends.containsAll(
                Set.of("dzmitry kapachou", "elena kapachova", "kseniya kapachova")));

        // friend must contain the person in friends list
        var friend = friendService.get(11200031L);
        Assert.assertTrue(friend.getFriends().stream()
                .map(Friend::getFriend)
                .anyMatch(f -> f.equals(String.format("%s %s", person.getFirstname(), person.getLastname()))));

        // remove previously added friend
        friendService.discardFriend(11200031L);
        var current = personService.get(11331003L);
        Assert.assertNotNull(current);
        var updatedAgainFriends = friendService.getFriends();
        Assert.assertFalse(updatedAgainFriends.isEmpty());
        Assert.assertEquals(updatedAgainFriends.size(), 2);
        Assert.assertTrue(updatedAgainFriends.containsAll(Set.of("dzmitry kapachou", "elena kapachova")));

        var removedFriend = friendService.get(11200031L);
        Assert.assertEquals(1, removedFriend.getFriends().size());
    }

}
