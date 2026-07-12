package dev.yazidcv.organization;

import dev.yazidcv.common.AuditedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity @Table(name="organizations")
public class Organization extends AuditedEntity {
    private String name; private String slug; private boolean active = true;
    protected Organization() {}
    public Organization(java.util.UUID id, String name, String slug) { this.id=id; this.name=name; this.slug=slug; }
    public String getName(){return name;} public String getSlug(){return slug;} public boolean isActive(){return active;}
}
