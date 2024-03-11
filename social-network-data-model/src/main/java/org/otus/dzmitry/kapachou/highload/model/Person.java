package org.otus.dzmitry.kapachou.highload.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "persons")
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"friends"})
public class Person extends BasicId implements Serializable {

    private String username;
    private String password;

    private String firstname, lastname, interests, city;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Friend> friends = new HashSet<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Tweet> tweets = new HashSet<>();


/*    @ManyToMany(mappedBy = "attenders", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Set<Dialogue> dialogues = new HashSet<>();*/

}
