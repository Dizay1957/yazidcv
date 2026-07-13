package dev.yazidcv.application;
import dev.yazidcv.common.AuditedEntity;
import jakarta.persistence.*;
import java.util.UUID;
@Entity @Table(name="candidate_applications",uniqueConstraints=@UniqueConstraint(columnNames={"candidate_id","job_id"}))
public class CandidateApplication extends AuditedEntity{
 @Column(name="organization_id",nullable=false) private UUID organizationId; @Column(name="candidate_id",nullable=false) private UUID candidateId; @Column(name="job_id",nullable=false) private UUID jobId;
 @Column(nullable=false) private String stage="APPLIED"; @Column(name="owner_id") private UUID ownerId; @Column(name="rejection_reason",length=500) private String rejectionReason; @Column(length=2000) private String notes; @Version private long version;
 protected CandidateApplication(){} public CandidateApplication(UUID org,UUID candidate,UUID job,UUID owner){organizationId=org;candidateId=candidate;jobId=job;ownerId=owner;}
 public void moveTo(String next,String reason,String notes){Stage current=Stage.valueOf(stage),target=Stage.valueOf(next);if(!current.canMoveTo(target))throw new IllegalArgumentException("Invalid pipeline transition from "+current+" to "+target);if(target==Stage.REJECTED&&(reason==null||reason.isBlank()))throw new IllegalArgumentException("A rejection reason is required.");stage=target.name();rejectionReason=target==Stage.REJECTED?reason:null;this.notes=notes;}
 public UUID getOrganizationId(){return organizationId;} public UUID getCandidateId(){return candidateId;} public UUID getJobId(){return jobId;} public String getStage(){return stage;} public UUID getOwnerId(){return ownerId;} public String getRejectionReason(){return rejectionReason;} public String getNotes(){return notes;}
 public enum Stage{APPLIED,SCREENING,SHORTLISTED,INTERVIEW,ASSESSMENT,OFFER,HIRED,REJECTED,WITHDRAWN;boolean canMoveTo(Stage n){if(this==HIRED||this==REJECTED||this==WITHDRAWN)return false;if(n==REJECTED||n==WITHDRAWN)return true;return switch(this){case APPLIED->n==SCREENING;case SCREENING->n==SHORTLISTED;case SHORTLISTED->n==INTERVIEW;case INTERVIEW->n==ASSESSMENT||n==OFFER;case ASSESSMENT->n==INTERVIEW||n==OFFER;case OFFER->n==HIRED;default->false;};}}
}
