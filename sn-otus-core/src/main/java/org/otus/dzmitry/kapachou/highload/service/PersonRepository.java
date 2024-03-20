package org.otus.dzmitry.kapachou.highload.service;

import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface PersonRepository extends BaseRepository<Person> {

    Person getByUsername(String name);

    Collection<Person> findByFirstnameAndLastnameOrderByIdAsc(String firstname, String lastname);

}
