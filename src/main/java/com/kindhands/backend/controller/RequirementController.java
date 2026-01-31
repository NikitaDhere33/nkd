package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Requirement;
import com.kindhands.backend.entity.RequirementStatus;
import com.kindhands.backend.repository.RequirementRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/requirements")
@CrossOrigin(origins = "*")
public class RequirementController {

    private final RequirementRepository requirementRepository;

    public RequirementController(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    // ================= CREATE (ORGANIZATION) =================
    @PostMapping
    public Requirement create(@RequestBody Requirement requirement) {

        // default values
        requirement.setStatus(RequirementStatus.PENDING);
        requirement.setCreatedAt(LocalDateTime.now());

        return requirementRepository.save(requirement);
    }

    // ================= ADMIN =================
    // admin ला सगळ्या requirements दिसतील
    @GetMapping
    public List<Requirement> getAll() {
        return requirementRepository.findAll();
    }

    // ================= DONOR =================
    // donor ला फक्त APPROVED requirements
    @GetMapping("/donor/approved")
    public List<Requirement> donorApprovedRequirements() {
        return requirementRepository.findByStatus(RequirementStatus.APPROVED);
    }

    // ================= ORGANIZATION =================
    // specific organization चे requirements
    @GetMapping("/organization/{orgId}")
    public List<Requirement> byOrganization(@PathVariable Long orgId) {
        return requirementRepository.findByOrganizationId(orgId);
    }
}
