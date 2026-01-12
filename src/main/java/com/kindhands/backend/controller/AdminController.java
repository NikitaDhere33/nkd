package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Organization;
import com.kindhands.backend.entity.OrganizationStatus;
import com.kindhands.backend.repository.OrganizationRepository;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final OrganizationRepository organizationRepository;

    public AdminController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    // 1️⃣ Admin sees all PENDING organizations
    @GetMapping("/organizations/pending")
    public List<Organization> getPendingOrganizations() {
        return organizationRepository.findByStatus(OrganizationStatus.PENDING);
    }

    // 2️⃣ Admin APPROVES organization
    @PutMapping("/organizations/{id}/approve")
    public Organization approveOrganization(@PathVariable Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        organization.setStatus(OrganizationStatus.APPROVED);
        return organizationRepository.save(organization);
    }

    // 3️⃣ Admin REJECTS organization
    @PutMapping("/organizations/{id}/reject")
    public Organization rejectOrganization(@PathVariable Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        organization.setStatus(OrganizationStatus.REJECTED);
        return organizationRepository.save(organization);
    }
    @GetMapping("/organizations/{id}/document")
    public byte[] viewOrganizationDocument(@PathVariable Long id) throws IOException {

        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        File file = new File(org.getDocumentPath());
        return java.nio.file.Files.readAllBytes(file.toPath());
    }
}
