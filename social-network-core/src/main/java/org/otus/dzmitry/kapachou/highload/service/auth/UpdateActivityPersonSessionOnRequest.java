package org.otus.dzmitry.kapachou.highload.service.auth;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.auth.AuthPerson;
import org.otus.dzmitry.kapachou.highload.service.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class UpdateActivityPersonSessionOnRequest extends GenericFilterBean {

    final SessionService sessionService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthPerson userDetails = (AuthPerson) authentication.getPrincipal();
        var authenticated = userDetails.getPerson();
        var session = sessionService.getSessionByPersonId(authenticated.getId());
        sessionService.updateLastActivityAtToCurrentTime(session.getId());
        chain.doFilter(request, response);
    }
}
