package org.otus.dzmitry.kapachou.highload.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseAuditIdEntity extends BasicId {

    @LastModifiedDate
    @Column(name = "modified_at")
    private Instant modifiedAt;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

}
