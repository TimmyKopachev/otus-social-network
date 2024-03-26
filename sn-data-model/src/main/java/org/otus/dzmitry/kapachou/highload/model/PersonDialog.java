package org.otus.dzmitry.kapachou.highload.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "persons_dialogues")
@Data
public class PersonDialog {

    @EmbeddedId
    private EmbeddedPersonDialogueKey key = new EmbeddedPersonDialogueKey();

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbeddedPersonDialogueKey implements Serializable {
        @Column(name = "person_id")
        private Long person_id;
        @Column(name = "dialogue_id")
        private Long dialogue_id;
    }


}
