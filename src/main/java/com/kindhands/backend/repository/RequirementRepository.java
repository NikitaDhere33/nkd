package com.kindhands.backend.repository;

import com.kindhands.backend.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    List<Requirement> findByOrganizationId(Long organizationId);

    List<Requirement> findByStatus(String status);
}
