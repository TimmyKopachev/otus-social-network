package org.otus.dzmitry.kapachou.highload.service;

import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;


@Repository
public interface PersonRepository extends BaseRepository<Person> {

    @EntityGraph(value = "graph.person.security-roles", type = EntityGraph.EntityGraphType.LOAD)
    Person getByUsername(String name);
    Optional<Person> getPersonByUsername(String name);

    Collection<Person> findByFirstnameAndLastnameOrderByIdAsc(String firstname, String lastname);

}
