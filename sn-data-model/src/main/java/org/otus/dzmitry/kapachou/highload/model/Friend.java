package org.otus.dzmitry.kapachou.highload.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "friends")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"owner", "friend"})
public class Friend implements Serializable {

    @EmbeddedId
    private EmbeddedPersonFriendKey key = new EmbeddedPersonFriendKey();

    @ManyToOne
    @MapsId("owner_id")
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @ManyToOne
    @MapsId("friend_id")
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private Person friend;

    public String getOwner() {
        return String.format("%s %s", owner.getFirstname(), owner.getLastname());
    }

    public String getFriend() {
        return String.format("%s %s", friend.getFirstname(), friend.getLastname());
    }

    @JsonIgnore
    public Person getFriendAsPerson() {
        return friend;
    }

    public Friend(Person owner, Person friend) {
        this.owner = owner;
        this.friend = friend;
        this.key = new EmbeddedPersonFriendKey(owner.getId(), friend.getId());
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbeddedPersonFriendKey implements Serializable {
        @Column(name = "owner_id")
        private Long ownerId;
        @Column(name = "friend_id")
        private Long friendId;
    }

    @Override
    public String toString() {
        return String.format("Friend to [%s-%s] is [%s-%s]",
                owner.getFirstname(), owner.getLastname(), friend.getFirstname(), friend.getLastname());
    }
}
