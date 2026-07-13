package dev.yazidcv.organization;

import dev.yazidcv.common.AuditedEntity;
import jakarta.persistence.*;
import java.util.UUID;

@Entity @Table(name="teams", uniqueConstraints=@UniqueConstraint(columnNames={"organization_id","name"}))
public class Team extends AuditedEntity {
 @Column(name="organization_id",nullable=false) private UUID organizationId;
 @Column(nullable=false,length=120) private String name;
 @Column(length=500) private String description;
 private boolean active=true;
 protected Team(){}
 public Team(UUID organizationId,String name,String description){this.organizationId=organizationId;this.name=name.trim();this.description=description;}
 public UUID getOrganizationId(){return organizationId;} public String getName(){return name;} public String getDescription(){return description;} public boolean isActive(){return active;}
}
