package org.otus.dzmitry.kapachou.highload.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.otus.dzmitry.kapachou.highload.jpa.AbstractCrudService;
import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.authentication.AuthenticatedPersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@AllArgsConstructor
@Service("personService")
@Slf4j
public class PersonService extends AbstractCrudService<Person> implements UserDetailsService {

    final PersonRepository personRepository;

    public Collection<Person> searchPersonsByFirstnameAndLastname(String firstname, String lastname) {
        return personRepository.findByFirstnameAndLastnameOrderByIdAsc(firstname, lastname);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.getPersonByUsername(username)
                .map(this::wrapToAuthenticatedPerson).orElseThrow(
                        () -> {
                            log.error(
                                    "PersonService.loadUserByUsername with player<{}> not found.", username);
                            return new UsernameNotFoundException(String.format("Player can not be found: %s", username));
                        });
    }

    public Person loadPersonByUsername(String username) {
        return personRepository.getByUsername(username);
    }

    public AuthenticatedPersonDetails wrapToAuthenticatedPerson(Person p) {
        var authenticated = new AuthenticatedPersonDetails();
        authenticated.wrapAuthorityRoles(p.getRoles());
        authenticated.setPerson(p);
        return authenticated;
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
