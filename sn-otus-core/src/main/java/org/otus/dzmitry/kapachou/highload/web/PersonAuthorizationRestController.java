package org.otus.dzmitry.kapachou.highload.web;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.payload.request.PersonRegistrationRequest;
import org.otus.dzmitry.kapachou.highload.payload.request.PersonSingInRequest;
import org.otus.dzmitry.kapachou.highload.payload.request.TokenRefreshRequest;
import org.otus.dzmitry.kapachou.highload.service.security.PersonSecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PersonAuthorizationRestController {

    private final PersonSecurityService personSecurityService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody PersonRegistrationRequest request) {
        return ResponseEntity.ok()
                .body(personSecurityService.signUp(wrapRegistrationToPerson(request), request.getRole()));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody PersonSingInRequest request) {
        return ResponseEntity.ok()
                .body(personSecurityService.signIn(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok().body(personSecurityService.refreshToken(request.getRefreshToken()));
    }

    private Person wrapRegistrationToPerson(PersonRegistrationRequest request) {
        var person = new Person();
        person.setUsername(request.getUsername());
        person.setPassword(request.getPassword());
        person.setFirstname(request.getFirstname());
        person.setLastname(request.getLastname());
        person.setCity(request.getCity());
        person.setAge(request.getAge());
        person.setGender(request.getGender());
        person.setInterests(request.getInterests());

        return person;
    }
}
