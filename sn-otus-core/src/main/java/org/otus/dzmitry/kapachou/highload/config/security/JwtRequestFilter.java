package org.otus.dzmitry.kapachou.highload.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.otus.dzmitry.kapachou.highload.model.authentication.AuthenticatedPersonDetails;
import org.otus.dzmitry.kapachou.highload.payload.AuthenticationTokens;
import org.otus.dzmitry.kapachou.highload.service.security.JwtPersonTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer";

    @Autowired
    @Qualifier(value = "personService")
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtPersonTokenService jwtPersonTokenService;
    @Autowired
    private JwtTokenProcessor jwtTokenProcessor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (StringUtils.hasText(token)) {
            var username = jwtTokenProcessor.getUsernameFromToken(token);
            if (username != null && jwtTokenProcessor.isValidToken(token)
                    && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                AuthenticatedPersonDetails authenticatedPersonDetails = (AuthenticatedPersonDetails) userDetailsService.loadUserByUsername(username);
                var person = authenticatedPersonDetails.getPerson();
                var accessToken = jwtPersonTokenService.findByPersonId(person.getId())
                        .orElseThrow(() -> new BadCredentialsException(String.format("Cannot fetch an access-token for username:<%s>", token)));
                if (jwtTokenProcessor.isValidToken(accessToken.getAccessToken(), authenticatedPersonDetails)) {
                    authenticatedPersonDetails.setAuthenticationTokens(
                            new AuthenticationTokens(accessToken.getAccessToken(), accessToken.getRefreshToken()));
                    UsernamePasswordAuthenticationToken upaToken =
                            new UsernamePasswordAuthenticationToken(
                                    authenticatedPersonDetails, null, authenticatedPersonDetails.getAuthorities());
                    upaToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(upaToken);
                    new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.getContext(), request, response);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String[] tokens = authHeader.trim().split("\\s+");
            if (tokens.length == 2 && BEARER.equalsIgnoreCase(tokens[0])) {
                return tokens[1];
            }
        }
        return null;
    }

}
