package org.otus.dzmitry.kapachou.highload.service.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.otus.dzmitry.kapachou.highload.config.security.JwtTokenProcessor;
import org.otus.dzmitry.kapachou.highload.exception.TokenRefreshException;
import org.otus.dzmitry.kapachou.highload.exception.UserCreationException;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.authentication.AuthenticatedPersonDetails;
import org.otus.dzmitry.kapachou.highload.model.authentication.JwtPersonToken;
import org.otus.dzmitry.kapachou.highload.payload.response.PersonAuthorizationResponse;
import org.otus.dzmitry.kapachou.highload.payload.response.PersonTokensResponse;
import org.otus.dzmitry.kapachou.highload.service.PersonService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class PersonSecurityService {

    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtPersonTokenService jwtPersonTokenService;
    private final JwtTokenProcessor jwtTokenProcessor;

    public PersonAuthorizationResponse signUp(Person person, String role) {
        Person loaded = personService.loadPersonByUsername(person.getUsername());
        if (loaded != null) {
            var message = String.format("User %s is already exist", person.getUsername());
            log.debug(message);
            throw new UserCreationException(message);
        }
        return savingPerson(person, role);
    }

    @Transactional
    private PersonAuthorizationResponse savingPerson(Person person, String roleName) {
        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Role %s hasnot been found.", roleName)));
        person.getRoles().add(role);
        person.encryptPassword(passwordEncoder);
        var saved = personService.save(person);
        log.info("Person has been successfully saved:<{}>", saved.getId());
        var refreshToken = jwtPersonTokenService.createRefreshToken(saved);
        return new PersonAuthorizationResponse(saved.getUsername(), refreshToken.getRefreshToken(), jwtTokenProcessor.generateToken(saved));
    }

    @Transactional
    public PersonAuthorizationResponse signIn(String username, String password) {
        return Optional.of(personService.loadUserByUsername(username))
                .filter(ud -> passwordEncoder.matches(password, ud.getPassword()))
                .map(ud -> {
                    var authenticated = (AuthenticatedPersonDetails) ud;
                    JwtPersonToken generatedJwtTokens = jwtPersonTokenService.createRefreshToken(authenticated.getPerson());
                    return new PersonAuthorizationResponse(ud.getUsername(), generatedJwtTokens.getAccessToken(), generatedJwtTokens.getRefreshToken());
                }).orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }

    @Transactional
    public PersonTokensResponse refreshToken(String refreshToken) {
        return jwtPersonTokenService.findByToken(refreshToken)
                .filter(t -> !jwtPersonTokenService.isTokenExpired(t))
                .map(JwtPersonToken::getPerson)
                .map(person -> {
                    final var savedToken = jwtPersonTokenService.createRefreshToken(person);
                    return new PersonTokensResponse(savedToken.getAccessToken(), savedToken.getRefreshToken());
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken));
    }

}
