package org.otus.dzmitry.kapachou.highload.jpa;

import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.auth.AuthPerson;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationCachedPersonService {

    public Person getAuthenticatedPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPerson userDetails = (AuthPerson) authentication.getPrincipal();
        return Optional.of(userDetails)
                .map(AuthPerson::getPerson)
                .orElseThrow();
    }
}
