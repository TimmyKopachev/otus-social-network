package org.otus.dzmitry.kapachou.highload.cache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tweets")
@EqualsAndHashCode(callSuper = true, exclude = "author")
public class Tweet extends BaseAuditIdEntity {

    private String text;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "person_id", nullable = false)
    private Person author;

    public String getAuthor() {
        return String.format("%s %s", author.getFirstname(), author.getLastname());
    }

    @JsonIgnore
    public Person getAuthorAsPerson() {
        return this.author;
    }

    @Override
    public String toString() {
        return String.format("author: [%s %s] wrote a post: %s %s %s",
                author.getFirstname(), author.getLastname(), text, getCreatedAt(), getId());
    }
}



