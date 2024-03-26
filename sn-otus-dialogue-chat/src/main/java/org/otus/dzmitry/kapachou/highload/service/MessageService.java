package org.otus.dzmitry.kapachou.highload.service;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.jpa.AbstractCrudService;
import org.otus.dzmitry.kapachou.highload.jpa.AuthenticationCachedPersonService;
import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Message;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MessageService extends AbstractCrudService<Message> {
    private final AuthenticationCachedPersonService authenticatedService;

    private MessageRepository messageRepository;
    private DialogueService dialogueService;

    public Message writeMessage(String text, Long dialogueId) {
        var current = authenticatedService.getAuthenticatedPerson();

        var dialogue = dialogueService.get(dialogueId);
        var msg = new Message();
        msg.setText(text);
        msg.setAuthorId(current.getId());
        msg.setDialogue(dialogue);
        return messageRepository.save(msg);
    }

    public Collection<Message> getMessagesByDialogue(Long dialogueId) {
        return messageRepository.findTop1000ByDialogueIdOrderByCreatedAtDesc(dialogueId);
    }

    @Override
    protected BaseRepository<Message> getRepository() {
        return messageRepository;
    }
}
