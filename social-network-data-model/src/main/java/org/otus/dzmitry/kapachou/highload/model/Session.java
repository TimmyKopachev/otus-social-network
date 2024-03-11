package org.otus.dzmitry.kapachou.highload.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.Instant;

@Data
@Table(name = "sessions")
@Entity
@EqualsAndHashCode(callSuper = true)
public class Session extends BasicId {

    @Column(name = "signed_in_at")
    private Instant signedInAt;
    @Column(name = "signed_out_at")
    private Instant signedOutAt;
    @Column(name = "last_activity_at")
    private Instant lastActivityAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public String getPerson() {
        return String.format("%s %s", person.getFirstname(), person.getLastname());
    }

}
