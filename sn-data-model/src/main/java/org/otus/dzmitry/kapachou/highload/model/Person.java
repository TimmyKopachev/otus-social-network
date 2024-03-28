package org.otus.dzmitry.kapachou.highload.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.otus.dzmitry.kapachou.highload.model.authentication.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "persons")
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"friends"})

@NamedEntityGraph(
        name = "graph.person.security-roles",
        attributeNodes = @NamedAttributeNode(value = "roles")
)
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

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "persons_roles",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

}
