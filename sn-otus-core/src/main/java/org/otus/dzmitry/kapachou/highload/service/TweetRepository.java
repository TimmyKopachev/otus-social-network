package org.otus.dzmitry.kapachou.highload.service;

import org.otus.dzmitry.kapachou.highload.cache.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.cache.model.Tweet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TweetRepository extends BaseRepository<Tweet> {

    @Query(nativeQuery = true,
            value = """
                    SELECT DISTINCT ftweets.* FROM
                    ((SELECT t.* FROM tweets t
                    WHERE t.person_id = :id)
                        UNION
                    (SELECT t.* FROM tweets t
                    INNER JOIN
                        (SELECT f.friend_id as fi FROM friends f
                         WHERE f.owner_id = :id) fids
                         ON fids.fi = t.person_id)) ftweets
                    ORDER BY created_at DESC
                    LIMIT 1000
                    """)
    Collection<Tweet> findTweetsWithFriendsTweetsByPersonId(Long id);
}
