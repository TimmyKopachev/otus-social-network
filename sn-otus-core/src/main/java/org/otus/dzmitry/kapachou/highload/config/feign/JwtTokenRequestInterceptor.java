package org.otus.dzmitry.kapachou.highload.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;

import java.util.Objects;


@AllArgsConstructor
public class JwtTokenRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer ";

    final AuthenticationCachedPersonService authenticationService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        var authenticated = authenticationService.getAuthenticated();
        if (Objects.nonNull(authenticated)) {
            var tokens = authenticated.getAuthenticationTokens();
            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s%s", TOKEN_TYPE, tokens.getAccessToken()));
        }
    }

}
