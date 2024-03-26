package org.otus.dzmitry.kapachou.highload.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AbstractCrudService;
import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.auth.AuthPerson;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class PersonService extends AbstractCrudService<Person> implements UserDetailsService {

    final PersonRepository personRepository;

    public Collection<Person> searchPersonsByFirstnameAndLastname(String firstname, String lastname) {
        return personRepository.findByFirstnameAndLastnameOrderByIdAsc(firstname, lastname);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthPerson(personRepository.getByUsername(username));
    }

    public List<Person> getPersonsByIds(Collection<Long> ids) {
        Iterable<Person> persons = personRepository.findAllById(ids);
        return StreamSupport.stream(persons.spliterator(), false)
                .collect(Collectors.toList());
    }

    public Collection<Person> getActivePersonSessions() {
        //initially all are active
        return Lists.newArrayList(personRepository.findAll());
    }


    @Override
    protected BaseRepository<Person> getRepository() {
        return personRepository;
    }
}
