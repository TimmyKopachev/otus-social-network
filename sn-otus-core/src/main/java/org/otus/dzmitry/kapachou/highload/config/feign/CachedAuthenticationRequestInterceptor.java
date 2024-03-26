package org.otus.dzmitry.kapachou.highload.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;

import java.util.Base64;


@AllArgsConstructor
public class CachedAuthenticationRequestInterceptor implements RequestInterceptor {

    final AuthenticationCachedPersonService authenticationService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        var authenticated = authenticationService.getAuthenticatedPerson();

        var token = base64Encode(String.format("%s:%s", authenticated.getUsername(), authenticated.getPassword()).getBytes());
        requestTemplate.header("Authorization", String.format("Basic %s", token));
    }

    private static String base64Encode(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

}
