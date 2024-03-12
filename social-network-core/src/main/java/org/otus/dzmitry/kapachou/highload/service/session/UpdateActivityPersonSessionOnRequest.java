package org.otus.dzmitry.kapachou.highload.service.session;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.auth.AuthPerson;
import org.otus.dzmitry.kapachou.highload.service.SessionService;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UpdateActivityPersonSessionOnRequest extends InMemoryHttpTraceRepository {

    final SessionService sessionService;

    @Override
    public List<HttpTrace> findAll() {
        return super.findAll();
    }

    @Override
    public void add(HttpTrace trace) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            AuthPerson userDetails = (AuthPerson) authentication.getPrincipal();
            var authenticated = userDetails.getPerson();
            var session = sessionService.getSessionByPersonId(authenticated.getId());
            sessionService.updateLastActivityAtToCurrentTime(session.getId());
        }
        super.add(trace);
    }
}
