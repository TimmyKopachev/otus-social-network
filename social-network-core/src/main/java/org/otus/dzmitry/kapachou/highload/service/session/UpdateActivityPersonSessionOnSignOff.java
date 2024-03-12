package org.otus.dzmitry.kapachou.highload.service.session;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.Session;
import org.otus.dzmitry.kapachou.highload.model.auth.AuthPerson;
import org.otus.dzmitry.kapachou.highload.service.SessionService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UpdateActivityPersonSessionOnSignOff {

    final SessionService sessionService;

    @EventListener
    public void onSuccessSignOff(LogoutSuccessEvent logoutSuccessEvent) {
        var authentication = logoutSuccessEvent.getAuthentication();
        AuthPerson principal = (AuthPerson) authentication.getPrincipal();
        var current = principal.getPerson();
        var session = sessionService.getSessionByPersonId(current.getId());
        Optional.of(session)
                .map(Session::getId)
                .ifPresentOrElse(sessionService::updateSignedOutAtToCurrentTime,
                        () -> {
                            throw new RuntimeException(String.format("Session activity for user<%s> must be presented.", current.getId()));
                        });
    }


}
