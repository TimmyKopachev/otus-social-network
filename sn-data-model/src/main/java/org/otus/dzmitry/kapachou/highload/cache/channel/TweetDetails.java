package org.otus.dzmitry.kapachou.highload.cache.channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetDetails {

    private Long tweetID;

    private String text;
    private String author;
    private Instant createdAt;


}
