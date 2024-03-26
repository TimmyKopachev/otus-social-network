package org.otus.dzmitry.kapachou.highload.service;

import org.otus.dzmitry.kapachou.highload.jpa.BaseRepository;
import org.otus.dzmitry.kapachou.highload.model.Message;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface MessageRepository extends BaseRepository<Message> {

    Collection<Message> findTop1000ByDialogueIdOrderByCreatedAtDesc(Long dialogueId);


}
