package org.otus.dzmitry.kapachou.highload.web.external;

import feign.Headers;
import org.otus.dzmitry.kapachou.highload.config.feign.SnFeignClientConfiguration;
import org.otus.dzmitry.kapachou.highload.model.Dialogue;
import org.otus.dzmitry.kapachou.highload.model.Message;
import org.otus.dzmitry.kapachou.highload.payload.request.CreateDialogueRequest;
import org.otus.dzmitry.kapachou.highload.payload.request.CreateMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

@FeignClient(name = "sn-otus-dialogue-chat", path = "/dialogues", configuration = SnFeignClientConfiguration.class)
@Headers("Content-Type: application/json")
public interface DialogueExternalRestApi {

    @RequestMapping(method = RequestMethod.POST)
    Dialogue createDialogue(@RequestBody CreateDialogueRequest request);

    @RequestMapping(method = RequestMethod.GET)
    Collection<Dialogue> getDialogues();

    @RequestMapping(method = RequestMethod.GET, value = "/{dialogueId}")
    Collection<Message> getDialogueById(@PathVariable Long dialogueId);

    @RequestMapping(method = RequestMethod.POST, value = "/{dialogueId}")
    Message writeMessageToDialogue(@PathVariable Long dialogueId,
                                   @RequestBody CreateMessageRequest request);

}
