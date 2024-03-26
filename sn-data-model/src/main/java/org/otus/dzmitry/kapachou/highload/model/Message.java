package org.otus.dzmitry.kapachou.highload.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "messages")
@ToString(exclude = "dialogue")
public class Message extends BaseAuditIdEntity {

    private String text;
    private Long authorId;
    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.RECEIVED;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "dialogue_id")
    private Dialogue dialogue;

    private enum MessageStatus {
        RECEIVED, VIEWED;
    }
}
