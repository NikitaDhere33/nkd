package com.kindhands.backend.service;

import com.kindhands.backend.entity.Requirement;
import com.kindhands.backend.entity.RequirementStatus;
import com.kindhands.backend.repository.RequirementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequirementService {

    private final RequirementRepository repo;

    public RequirementService(RequirementRepository repo) {
        this.repo = repo;
    }

    public Requirement createRequirement(Requirement req) {
        req.setStatus(RequirementStatus.OPEN);
        req.setCreatedAt(LocalDateTime.now());
        return repo.save(req);
    }

    public List<Requirement> getOrgRequirements(Long orgId) {
        return repo.findByOrganizationId(orgId);
    }

    public List<Requirement> getAllOpenRequirements() {
        return repo.findByStatus("OPEN");
    }
}
