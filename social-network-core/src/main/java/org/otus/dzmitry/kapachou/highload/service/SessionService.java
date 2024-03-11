package org.otus.dzmitry.kapachou.highload.service;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.Person;
import org.otus.dzmitry.kapachou.highload.model.Session;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@AllArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Session generateSessionFor(Person person) {
        Session session = new Session();
        session.setPerson(person);
        return sessionRepository.save(session);
    }

    public Session getSessionByPersonId(Long personId) {
        return sessionRepository.getSessionByPersonId(personId);
    }

    @Transactional
    public void updateSignInAtToCurrentTime(Long sessionId) {
        sessionRepository.updateSignInAtToCurrentTime(sessionId);
    }

    @Transactional
    public void updateSignedOutAtToCurrentTime(Long sessionId) {
        sessionRepository.updateSignedOutAtToCurrentTime(sessionId);
    }

    @Transactional
    public void updateLastActivityAtToCurrentTime(Long sessionId) {
        sessionRepository.updateLastActivityAtToCurrentTime(sessionId);
    }

}
