package dev.yazidcv.cv;
import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface CvDocumentRepository extends JpaRepository<CvDocument,UUID>{Page<CvDocument> findByOrganizationId(UUID org,Pageable page);boolean existsByOrganizationIdAndChecksum(UUID org,String checksum);long countByOrganizationIdAndStatus(UUID org,String status);}
