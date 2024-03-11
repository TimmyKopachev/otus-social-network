package org.otus.dzmitry.kapachou.highload.service.auth;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.auth.AuthPerson;
import org.otus.dzmitry.kapachou.highload.service.SessionService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class UpdateActivityPersonSessionOnCompletedSignIn {

    final SessionService sessionService;

    @EventListener
    public void onSuccessSignIn(AuthenticationSuccessEvent success) {
        AuthPerson principal = (AuthPerson) success.getAuthentication().getPrincipal();
        var current = principal.getPerson();
        var session = sessionService.getSessionByPersonId(current.getId());
        if (Objects.isNull(session)) {
            session = sessionService.generateSessionFor(current);
        }
        sessionService.updateSignInAtToCurrentTime(session.getId());
    }

}
