package org.otus.dzmitry.kapachou.highload.cache.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.otus.dzmitry.kapachou.highload.cache.SocialNetworkCoreIntegrationTest;
import org.otus.dzmitry.kapachou.highload.cache.model.Person;
import org.otus.dzmitry.kapachou.highload.cache.model.auth.AuthPerson;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonRepositoryIntegrationTest extends SocialNetworkCoreIntegrationTest {

    @Autowired
    PersonService personService;

    @Test
    public void verifyPersonCreatedSuccessfully() {
        var result = personService.findAll();
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(result.size(), 4);

        Person saved = personService.save(buildPerson());
        var persons = personService.findAll();
        Assert.assertEquals(5, persons.size());
        Assert.assertNotNull(saved);
    }

    @Test
    public void verifyPersonReturnByUserName() {
        AuthPerson result = (AuthPerson) personService.loadUserByUsername("username-1");
        var person = result.getPerson();
        Assert.assertNotNull(person);
        Assert.assertEquals("dzmitry", person.getFirstname());
        Assert.assertEquals("kapachou", person.getLastname());
    }

    @Test
    public void verifyNullForUsernameIsNotExists() {
        AuthPerson result = (AuthPerson) personService.loadUserByUsername("unknown-player-username");
        var person = result.getPerson();
        Assert.assertNull(person);
    }

    private static Person buildPerson() {
        Person person = new Person();
        person.setUsername("new-username-for-test");
        person.setPassword("password");
        person.setFirstname("John");
        person.setLastname("Desmond-II");
        return person;
    }


}
