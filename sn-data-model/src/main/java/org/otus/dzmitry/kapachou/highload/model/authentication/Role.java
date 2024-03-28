package org.otus.dzmitry.kapachou.highload.model.authentication;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.otus.dzmitry.kapachou.highload.model.BasicId;
import org.otus.dzmitry.kapachou.highload.model.Person;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "roles")
@ToString(exclude = "persons")
public class Role extends BasicId {

    private String name;

    @ManyToMany(
            mappedBy = "roles",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE})
    private List<Person> persons;
}
