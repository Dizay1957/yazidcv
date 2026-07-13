package dev.yazidcv.application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CandidateApplicationRepository extends JpaRepository<CandidateApplication,UUID>{List<CandidateApplication> findAllByOrganizationIdOrderByUpdatedAtDesc(UUID organizationId);Optional<CandidateApplication> findByIdAndOrganizationId(UUID id,UUID organizationId);}
