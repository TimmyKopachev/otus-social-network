package org.otus.dzmitry.kapachou.highload.model.authentication;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.otus.dzmitry.kapachou.highload.model.BasicId;
import org.otus.dzmitry.kapachou.highload.model.Person;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "jwt_tokens")
public class JwtPersonToken extends BasicId {

    private String accessToken;

    private String refreshToken;

    private Instant issuedDate;

    private Instant expirationDate;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;
}
