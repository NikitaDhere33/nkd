package com.kindhands.backend.repository;

import com.kindhands.backend.entity.Requirement;
import com.kindhands.backend.entity.RequirementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    List<Requirement> findByOrganizationId(Long orgId);
    List<Requirement> findByStatus(RequirementStatus status);


}
