package org.otus.dzmitry.kapachou.highload.service;

import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Dialogue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DialogueRepository extends BaseRepository<Dialogue> {

/*    @Query(nativeQuery = true,
            value = """


                        SELECT DISTINCT d.*, d.created_at as d_created_at, msg.created_at as msg_created_at FROM dialogues d
                        INNER JOIN persons_dialogues pd
                            ON pd.dialogue_id = d.id
                        LEFT JOIN (
                            SELECT m.dialogue_id as md_id, MAX(m.created_at) as created_at
                            FROM messages m
                            GROUP BY m.dialogue_id
                        ) as msg ON msg.md_id = d.id            
                        WHERE pd.person_id = :attenderId
                        ORDER BY COALESCE(msg.created_at, d.created_at) DESC
                        LIMIT 100
                    """)*/
@Query(nativeQuery = true,
        value = """
                    SELECT * FROM (    
                        SELECT DISTINCT d.*, d.created_at as d_created_at, msg.created_at as msg_created_at FROM dialogues d
                        INNER JOIN persons_dialogues pd
                            ON pd.dialogue_id = d.id
                        LEFT JOIN (
                            SELECT m.dialogue_id as md_id, MAX(m.created_at) as created_at
                            FROM messages m
                            GROUP BY m.dialogue_id
                        ) as msg ON msg.md_id = d.id            
                        WHERE pd.person_id = :attenderId
                        LIMIT 100
                    ) as qr
                    ORDER BY COALESCE(qr.d_created_at, qr.msg_created_at) DESC NULLS LAST
                    """)
    Collection<Dialogue> findDialoguesForAttenderId(Long attenderId);

}
