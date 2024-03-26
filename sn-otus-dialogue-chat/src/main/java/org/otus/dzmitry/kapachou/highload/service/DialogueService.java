package org.otus.dzmitry.kapachou.highload.service;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AbstractCrudService;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;
import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Dialogue;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
@AllArgsConstructor
public class DialogueService extends AbstractCrudService<Dialogue> {

    private final DialogueRepository dialogueRepository;

    private final AuthenticationCachedPersonService authenticatedService;

    public Dialogue createDialogue(String title, Set<Long> attenderIds) {
        var current = authenticatedService.getAuthenticatedPerson();

        Dialogue dialogue = new Dialogue();
        dialogue.setTitle(title);
        dialogue.getAttenderIds().addAll(attenderIds);
        dialogue.getAttenderIds().add(current.getId());
        return dialogueRepository.save(dialogue);
    }

    public Collection<Dialogue> findDialoguesForAttenderId() {
        var current = authenticatedService.getAuthenticatedPerson();
        return dialogueRepository.findDialoguesForAttenderId(current.getId());
    }

    @Override
    protected BaseRepository<Dialogue> getRepository() {
        return dialogueRepository;
    }
}
