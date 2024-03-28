package org.otus.dzmitry.kapachou.highload.web;

import lombok.AllArgsConstructor;
import org.otus.dzmitry.kapachou.highload.model.Dialogue;
import org.otus.dzmitry.kapachou.highload.model.Message;
import org.otus.dzmitry.kapachou.highload.payload.request.CreateDialogueRequest;
import org.otus.dzmitry.kapachou.highload.payload.request.CreateMessageRequest;
import org.otus.dzmitry.kapachou.highload.service.DialogueService;
import org.otus.dzmitry.kapachou.highload.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/dialogues")
public class DialogueRestController {

    final DialogueService dialogueService;
    final MessageService messageService;
    final SessionRepository sessionRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dialogue createDialogue(@RequestBody CreateDialogueRequest request) {
        return dialogueService.createDialogue(request.getTitle(), request.getAttenderIds());
    }

    @GetMapping
    public Collection<Dialogue> getDialogues() {
        return dialogueService.findDialoguesForAttenderId();
    }

    @GetMapping("/{dialogueId}")
    public Collection<Message> getDialogueById(@PathVariable Long dialogueId) {
        return messageService.getMessagesByDialogue(dialogueId);
    }

    @PostMapping("/{dialogueId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Message writeMessageToDialogue(@PathVariable Long dialogueId,
                                          @RequestBody CreateMessageRequest request) {
        return messageService.writeMessage(request.getText(), dialogueId);
    }

}
