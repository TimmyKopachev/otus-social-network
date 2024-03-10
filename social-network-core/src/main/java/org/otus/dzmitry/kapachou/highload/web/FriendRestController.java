package org.otus.dzmitry.kapachou.highload.web;


import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.service.FriendService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/friends")
@AllArgsConstructor
public class FriendRestController {

    final FriendService friendService;

    @PostMapping
    public void addPersonAsFriend(@RequestParam Long friendId) {
        friendService.addAsFriend(friendId);
    }

    @DeleteMapping
    public void removePersonFromFriends(@RequestParam Long friendId) {
        friendService.discardFriend(friendId);
    }

    @GetMapping
    public Collection<String> friends() {
        return friendService.getFriends();
    }
}
