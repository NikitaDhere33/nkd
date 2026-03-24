package com.kindhands.backend.repository;

import com.kindhands.backend.entity.Requirement;
import com.kindhands.backend.entity.RequirementStatus; // Make sure this is imported
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    // This method is for your getOrgRequirements function
    List<Requirement> findByOrganizationId(Long orgId);

    // This is the required method for getAllOpenRequirements
    List<Requirement> findByStatus(RequirementStatus status);


}
