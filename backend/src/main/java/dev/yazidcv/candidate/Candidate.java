package dev.yazidcv.candidate;

import dev.yazidcv.common.AuditedEntity;
import jakarta.persistence.*;
import java.util.*;

@Entity @Table(name="candidates")
public class Candidate extends AuditedEntity {
    @Column(name="organization_id",nullable=false) private UUID organizationId;
    @Column(name="first_name",nullable=false) private String firstName;
    @Column(name="last_name",nullable=false) private String lastName;
    private String email; private String phone;
    @Column(name="current_title") private String currentTitle;
    @Column(name="current_company") private String currentCompany;
    private String city; private String country;
    @Column(name="years_experience",nullable=false) private int yearsExperience;
    private String status; private String source;
    @Column(name="professional_summary",length=4000) private String professionalSummary;
    @Column(length=2000) private String skills;
    @Column(name="match_score") private int matchScore;
    @Column(name="linkedin_url") private String linkedinUrl; @Column(name="github_url") private String githubUrl;
    @Column(name="spoken_languages",length=1000) private String spokenLanguages;
    @Column(name="education_summary",length=4000) private String educationSummary;
    @Column(name="experience_summary",length=6000) private String experienceSummary;
    @Column(name="projects_summary",length=6000) private String projectsSummary;
    @Column(name="extraction_confidence") private int extractionConfidence;
    private boolean active=true;
    @Version private long version;
    protected Candidate() {}
    public Candidate(UUID org,String first,String last){organizationId=org;firstName=first;lastName=last;status="NEW";source="MANUAL_UPLOAD";}
    public void update(String first,String last,String email,String phone,String title,String company,String city,String country,int years,String status,String summary,List<String> skills){this.firstName=first;this.lastName=last;this.email=email;this.phone=phone;this.currentTitle=title;this.currentCompany=company;this.city=city;this.country=country;this.yearsExperience=years;this.status=status;this.professionalSummary=summary;this.skills=String.join(",",skills);}
    public void seed(String email,String title,String company,String city,String country,int years,String status,int score,List<String> skills,String summary){update(firstName,lastName,email,null,title,company,city,country,years,status,summary,skills);matchScore=score;}
    public void applyCvInsight(String linkedin,String github,List<String> languages,String education,String experience,String projects,int confidence){linkedinUrl=linkedin;githubUrl=github;spokenLanguages=String.join(",",languages);educationSummary=education;experienceSummary=experience;projectsSummary=projects;extractionConfidence=confidence;}
    public void archive(){active=false;status="ARCHIVED";}
    public UUID getOrganizationId(){return organizationId;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;} public String getEmail(){return email;} public String getPhone(){return phone;} public String getCurrentTitle(){return currentTitle;} public String getCurrentCompany(){return currentCompany;} public String getCity(){return city;} public String getCountry(){return country;} public int getYearsExperience(){return yearsExperience;} public String getStatus(){return status;} public String getSource(){return source;} public String getProfessionalSummary(){return professionalSummary;} public List<String> getSkills(){return skills==null||skills.isBlank()?List.of():Arrays.stream(skills.split(",")).map(String::trim).toList();} public int getMatchScore(){return matchScore;} public boolean isActive(){return active;} public String getLinkedinUrl(){return linkedinUrl;} public String getGithubUrl(){return githubUrl;} public List<String> getSpokenLanguages(){return spokenLanguages==null||spokenLanguages.isBlank()?List.of():Arrays.stream(spokenLanguages.split(",")).toList();} public String getEducationSummary(){return educationSummary;} public String getExperienceSummary(){return experienceSummary;} public String getProjectsSummary(){return projectsSummary;} public int getExtractionConfidence(){return extractionConfidence;}
}
