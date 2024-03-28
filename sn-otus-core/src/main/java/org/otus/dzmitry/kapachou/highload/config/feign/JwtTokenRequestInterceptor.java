package org.otus.dzmitry.kapachou.highload.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;

import java.util.Base64;
import java.util.Objects;


@AllArgsConstructor
public class JwtTokenRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    //private static final String TOKEN_TYPE = "Bearer ";
    private static final String TOKEN_TYPE = "Basic ";

    final AuthenticationCachedPersonService authenticationService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        var authenticated = authenticationService.getAuthenticated();
        if (Objects.nonNull(authenticated)) {
            //var tokens = authenticated.getAuthenticationTokens();
            var token = Base64.getEncoder().encode(String.format("%s:%s", authenticated.getUsername(), authenticated.getPassword()).getBytes());
           // requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s%s", TOKEN_TYPE, tokens.getAccessToken()));
            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s%s", TOKEN_TYPE, new String(token)));
        }
    }

}
