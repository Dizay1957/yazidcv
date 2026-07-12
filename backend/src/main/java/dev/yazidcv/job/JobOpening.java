package dev.yazidcv.job;
import dev.yazidcv.common.AuditedEntity; import jakarta.persistence.*; import java.time.LocalDate; import java.util.*;
@Entity @Table(name="jobs")
public class JobOpening extends AuditedEntity{
 @Column(name="organization_id") private UUID organizationId; private String title; private String department; private String location;
 @Column(name="workplace_type") private String workplaceType; @Column(length=6000) private String description; @Column(name="required_skills",length=2000) private String requiredSkills;
 private String status; @Column(name="candidate_count") private int candidateCount; @Column(name="closing_date") private LocalDate closingDate; @Version private long version;
 protected JobOpening(){} public JobOpening(UUID org,String title){this.organizationId=org;this.title=title;status="DRAFT";}
 public void update(String title,String department,String location,String workplace,String description,List<String> skills,String status,LocalDate closing){this.title=title;this.department=department;this.location=location;this.workplaceType=workplace;this.description=description;this.requiredSkills=String.join(",",skills);this.status=status;this.closingDate=closing;}
 public void seed(String dep,String loc,String workplace,List<String> skills,String status,int count,LocalDate closing){update(title,dep,loc,workplace,"Recruitment role for "+title,skills,status,closing);candidateCount=count;}
 public UUID getOrganizationId(){return organizationId;} public String getTitle(){return title;} public String getDepartment(){return department;} public String getLocation(){return location;} public String getWorkplaceType(){return workplaceType;} public String getDescription(){return description;} public List<String> getRequiredSkills(){return requiredSkills==null?List.of():Arrays.stream(requiredSkills.split(",")).map(String::trim).toList();} public String getStatus(){return status;} public int getCandidateCount(){return candidateCount;} public LocalDate getClosingDate(){return closingDate;}
}
