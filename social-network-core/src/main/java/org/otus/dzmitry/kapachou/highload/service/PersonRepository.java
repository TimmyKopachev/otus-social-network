package org.otus.dzmitry.kapachou.highload.service;

import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.otus.dzmitry.kapachou.highload.model.Person;

import java.util.Collection;


@Repository
public interface PersonRepository extends BaseRepository<Person> {

    Person getByUsername(String name);

    Collection<Person> findByFirstnameAndLastnameOrderByIdAsc(String firstname, String lastname);


    @Query(value = """
            SELECT p.* FROM sessions s
            INNER JOIN persons p ON p.id = s.person_id
            WHERE s.last_activity_at > LOCALTIMESTAMP - INTERVAL '7 days'
            """, nativeQuery = true)
    Collection<Person> getRecentPersonSessions();


    @Query(value = """
            SELECT p.* FROM sessions s
            INNER JOIN persons p ON p.id = s.person_id
            WHERE s.last_activity_at AFTER s.signed_out_at 
            """, nativeQuery = true)
    Collection<Person> getActivePlayerSession();

}
