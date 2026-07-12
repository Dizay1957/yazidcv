package dev.yazidcv.review;
import dev.yazidcv.common.AuditedEntity; import jakarta.persistence.*; import java.util.UUID;
@Entity @Table(name="review_tasks") public class ReviewTask extends AuditedEntity{
 @Column(name="organization_id") private UUID organizationId; @Column(name="candidate_id") private UUID candidateId; private String reason; private int confidence; private String priority; private String status;
 @Column(name="reviewer_email") private String reviewerEmail; @Column(length=2000) private String notes;
 protected ReviewTask(){} public ReviewTask(UUID org,UUID candidate,String reason,int confidence,String priority){organizationId=org;candidateId=candidate;this.reason=reason;this.confidence=confidence;this.priority=priority;status="PENDING";}
 public void transition(String status,String reviewer,String notes){this.status=status;this.reviewerEmail=reviewer;this.notes=notes;}
 public UUID getOrganizationId(){return organizationId;} public UUID getCandidateId(){return candidateId;} public String getReason(){return reason;} public int getConfidence(){return confidence;} public String getPriority(){return priority;} public String getStatus(){return status;} public String getReviewerEmail(){return reviewerEmail;} public String getNotes(){return notes;}
}
