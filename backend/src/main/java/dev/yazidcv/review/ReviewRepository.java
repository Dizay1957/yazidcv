package dev.yazidcv.review;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ReviewRepository extends JpaRepository<ReviewTask,UUID>{List<ReviewTask> findByOrganizationIdAndStatusOrderByCreatedAtAsc(UUID org,String status);Optional<ReviewTask> findByIdAndOrganizationId(UUID id,UUID org);long countByOrganizationIdAndStatus(UUID org,String status);}
