package org.otus.dzmitry.kapachou.highload.web;


import org.otus.dzmitry.kapachou.highload.model.Dialogue;
import org.otus.dzmitry.kapachou.highload.model.Message;
import org.otus.dzmitry.kapachou.highload.request.CreateDialogueRequest;
import org.otus.dzmitry.kapachou.highload.request.CreateMessageRequest;
import org.otus.dzmitry.kapachou.highload.web.external.DialogueExternalRestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/dialogues")
class DialogueRestController {

    @Autowired
    DialogueExternalRestApi dialogueExternalRestApi;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dialogue createDialogue(CreateDialogueRequest request) {
        return dialogueExternalRestApi.createDialogue(request);
    }

    @GetMapping
    public Collection<Dialogue> getDialogues() {
        return dialogueExternalRestApi.getDialogues();
    }

    @GetMapping("/{dialogueId}")
    public Collection<Message> getDialogueById(Long dialogueId) {
        return dialogueExternalRestApi.getDialogueById(dialogueId);
    }

    @PostMapping("/{dialogueId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Message writeMessageToDialogue(Long dialogueId, CreateMessageRequest request) {
        return dialogueExternalRestApi.writeMessageToDialogue(dialogueId, request);
    }
}
