package dev.yazidcv.candidate;
import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;
import java.util.*;
public interface CandidateRepository extends JpaRepository<Candidate,UUID>{
 Page<Candidate> findByOrganizationIdAndActiveTrue(UUID org,Pageable pageable);
 @Query("select c from Candidate c where c.organizationId=:org and c.active=true and (lower(concat(c.firstName,' ',c.lastName)) like lower(concat('%',:q,'%')) or lower(coalesce(c.currentTitle,'')) like lower(concat('%',:q,'%')) or lower(coalesce(c.skills,'')) like lower(concat('%',:q,'%'))) ")
 Page<Candidate> search(@Param("org") UUID org,@Param("q") String query,Pageable pageable);
 long countByOrganizationIdAndActiveTrue(UUID org);
 long countByOrganizationIdAndActiveTrueAndCreatedAtAfter(UUID org,java.time.Instant since);
 List<Candidate> findByOrganizationIdAndCreatedAtAfter(UUID org,java.time.Instant since);
 Optional<Candidate> findByIdAndOrganizationId(UUID id,UUID org);
 Optional<Candidate> findFirstByOrganizationIdAndEmailIgnoreCase(UUID org,String email);
}
