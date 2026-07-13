package dev.yazidcv.application;
import dev.yazidcv.candidate.CandidateRepository; import dev.yazidcv.identity.TenantContext; import dev.yazidcv.job.JobRepository;
import jakarta.validation.Valid; import jakarta.validation.constraints.NotNull; import org.springframework.http.HttpStatus; import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/applications") public class CandidateApplicationController{
 private final CandidateApplicationRepository applications;private final CandidateRepository candidates;private final JobRepository jobs;private final TenantContext tenant;
 public CandidateApplicationController(CandidateApplicationRepository a,CandidateRepository c,JobRepository j,TenantContext t){applications=a;candidates=c;jobs=j;tenant=t;}
 @GetMapping List<View> list(JwtAuthenticationToken auth){return applications.findAllByOrganizationIdOrderByUpdatedAtDesc(tenant.organizationId(auth)).stream().map(View::of).toList();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @Transactional View create(@Valid @RequestBody Create r,JwtAuthenticationToken auth){UUID org=tenant.organizationId(auth);candidates.findByIdAndOrganizationId(r.candidateId(),org).orElseThrow();jobs.findByIdAndOrganizationId(r.jobId(),org).orElseThrow();return View.of(applications.save(new CandidateApplication(org,r.candidateId(),r.jobId(),null)));}
 @PatchMapping("/{id}/stage") @Transactional View move(@PathVariable UUID id,@Valid @RequestBody Move r,JwtAuthenticationToken auth){var a=applications.findByIdAndOrganizationId(id,tenant.organizationId(auth)).orElseThrow();a.moveTo(r.stage().toUpperCase(),r.rejectionReason(),r.notes());return View.of(a);}
 record Create(@NotNull UUID candidateId,@NotNull UUID jobId){} record Move(@NotNull String stage,String rejectionReason,String notes){}
 record View(UUID id,UUID candidateId,UUID jobId,String stage,UUID ownerId,String rejectionReason,String notes,java.time.Instant updatedAt){static View of(CandidateApplication a){return new View(a.getId(),a.getCandidateId(),a.getJobId(),a.getStage(),a.getOwnerId(),a.getRejectionReason(),a.getNotes(),a.getUpdatedAt());}}
}
