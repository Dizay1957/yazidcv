package dev.yazidcv.common;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class AuditedEntity {
    @Id protected UUID id;
    @Column(name="created_at", nullable=false) protected Instant createdAt;
    @Column(name="updated_at", nullable=false) protected Instant updatedAt;
    @PrePersist void create() { if (id == null) id = UUID.randomUUID(); createdAt = updatedAt = Instant.now(); }
    @PreUpdate void update() { updatedAt = Instant.now(); }
    public UUID getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
