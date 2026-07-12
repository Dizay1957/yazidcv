package dev.yazidcv.job;
import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface JobRepository extends JpaRepository<JobOpening,UUID>{Page<JobOpening> findByOrganizationId(UUID org,Pageable p);Optional<JobOpening> findByIdAndOrganizationId(UUID id,UUID org);long countByOrganizationIdAndStatus(UUID org,String status);}
