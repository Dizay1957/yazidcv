package dev.yazidcv.organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TeamRepository extends JpaRepository<Team,UUID>{List<Team> findAllByOrganizationIdAndActiveTrueOrderByName(UUID organizationId);Optional<Team> findByIdAndOrganizationId(UUID id,UUID organizationId);}
