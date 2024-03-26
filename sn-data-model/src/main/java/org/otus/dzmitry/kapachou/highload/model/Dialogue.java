package org.otus.dzmitry.kapachou.highload.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "dialogues")
@ToString(exclude = "messages")
public class Dialogue extends BaseAuditIdEntity {

    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "persons_dialogues",
            joinColumns = @JoinColumn(name = "dialogue_id"))
    @Column(name = "person_id")
    private Set<Long> attenderIds = new HashSet<>();

    @OneToMany(mappedBy = "dialogue", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Message> messages = Collections.emptySet();

}
