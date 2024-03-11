package org.otus.dzmitry.kapachou.highload.service;

import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface SessionRepository extends BaseRepository<Session> {

/*    @Query(value = """
            SELECT s.* FROM sessions s
            WHERE s.player_id = :playerId
            """, nativeQuery = true)*/
    Session getSessionByPersonId(Long playerId);


    @Modifying
    @Query(value = """
            UPDATE Session s
            SET s.signedInAt = current_time
            WHERE s.id = :sessionId
            """)
    void updateSignInAtToCurrentTime(Long sessionId);

    @Modifying
    @Query(value = """
            UPDATE Session s
            SET s.signedInAt = current_time
            WHERE s.id = :sessionId
            """)
    void updateSignedOutAtToCurrentTime(Long sessionId);

    @Modifying
    @Query(value = """
            UPDATE Session s
            SET s.signedInAt = current_time
            WHERE s.id = :sessionId
            """)
    void updateLastActivityAtToCurrentTime(Long sessionId);

}