package org.otus.dzmitry.kapachou.highload.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SnDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final String PROPERTY_CREDENTIALS_NOT_PROVIDED =
            "AbstractUserDetailsAuthenticationProvider.badCredentials";
    private static final String PROPERTY_CREDENTIALS_INCORRENT_PASSWORD =
            "AbstractUserDetailsAuthenticationProvider.password.incorrect";
    private static final String BAD_CREDENTIALS = "Bad credentials";
    private static final String USER_DETAIL_NOT_FOUND =
            "UserDetailsService returned null, which is an interface contract violation";

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(
            String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
        try {
            return Optional.ofNullable(this.userDetailsService.loadUserByUsername(username))
                    .orElseThrow(() -> new InternalAuthenticationServiceException(USER_DETAIL_NOT_FOUND));
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }
}
