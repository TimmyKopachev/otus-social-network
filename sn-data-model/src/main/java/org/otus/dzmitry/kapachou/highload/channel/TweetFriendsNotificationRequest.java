package org.otus.dzmitry.kapachou.highload.channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetFriendsNotificationRequest {

    private TweetDetails tweet;
    private String friendPrincipalReceiver;

}
