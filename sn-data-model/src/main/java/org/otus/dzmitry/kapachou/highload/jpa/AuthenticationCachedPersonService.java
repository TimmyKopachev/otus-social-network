package org.otus.dzmitry.kapachou.highload.jpa;

import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.authentication.AuthenticatedPersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationCachedPersonService {

    public Person getAuthenticatedPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedPersonDetails userDetails = (AuthenticatedPersonDetails) authentication.getPrincipal();
        return Optional.of(userDetails)
                .map(AuthenticatedPersonDetails::getPerson)
                .orElseThrow();
    }

    public AuthenticatedPersonDetails getAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AuthenticatedPersonDetails) authentication.getPrincipal();
    }
}
