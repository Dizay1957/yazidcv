package dev.yazidcv.identity;

import dev.yazidcv.common.AuditedEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="app_users")
public class AppUser extends AuditedEntity {
 @Column(name="organization_id",nullable=false) private UUID organizationId;
 @Column(name="team_id") private UUID teamId;
 @Column(name="first_name",nullable=false) private String firstName;
 @Column(name="last_name",nullable=false) private String lastName;
 @Column(nullable=false,unique=true) private String email;
 @Column(name="password_hash",nullable=false) private String passwordHash;
 @Column(nullable=false) private String role;
 @Column(nullable=false) private String locale="en";
 private boolean active=true;
 @Column(name="password_changed_at",nullable=false) private Instant passwordChangedAt=Instant.now();
 @Column(name="failed_login_attempts",nullable=false) private int failedLoginAttempts;
 @Column(name="locked_until") private Instant lockedUntil;
 protected AppUser(){}
 public AppUser(UUID organizationId,UUID teamId,String firstName,String lastName,String email,String passwordHash,String role,String locale){this.organizationId=organizationId;this.teamId=teamId;this.firstName=firstName.trim();this.lastName=lastName.trim();this.email=email.trim().toLowerCase();this.passwordHash=passwordHash;this.role=role;this.locale=locale;}
 public UUID getOrganizationId(){return organizationId;} public UUID getTeamId(){return teamId;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;} public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;} public String getRole(){return role;} public String getLocale(){return locale;} public boolean isActive(){return active;} public Instant getLockedUntil(){return lockedUntil;}
 public void deactivate(){active=false;}
}
