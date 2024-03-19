package org.otus.dzmitry.kapachou.highload.web;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.cache.model.Person;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class PersonRestController {

    final PersonService personService;

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id) {
        return personService.get(id);
    }

    @GetMapping("/search")
    public Collection<Person> getPersonByFirstNameAndLastName(@RequestParam String firstname, @RequestParam String lastname) {
        return personService.searchPersonsByFirstnameAndLastname(firstname, lastname);
    }

    @GetMapping
    public Collection<Person> findAllPersons() {
        return personService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person createUser(@RequestBody Person person) {
        return personService.save(person);
    }

}
