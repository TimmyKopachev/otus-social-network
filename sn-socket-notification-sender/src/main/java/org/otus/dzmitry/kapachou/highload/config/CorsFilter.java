package org.otus.dzmitry.kapachou.highload.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.*;

@Component
public class CorsFilter extends org.springframework.web.filter.CorsFilter {

    public CorsFilter(CorsConfigurationSource configSource) {
        super(configSource);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.addHeader(ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS");
        response.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Cache-Control");
        response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.addIntHeader(ACCESS_CONTROL_MAX_AGE, 31536000);
        super.doFilterInternal(request, response, filter);
    }

}
