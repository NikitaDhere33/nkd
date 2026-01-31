package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Organization;
import com.kindhands.backend.entity.OrganizationStatus;
import com.kindhands.backend.repository.OrganizationRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final OrganizationRepository organizationRepository;

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "orgs";

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
    public ResponseEntity<?> approveOrganization(@PathVariable Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        organization.setStatus(OrganizationStatus.APPROVED);
        organizationRepository.save(organization);

        return ResponseEntity.ok("Organization approved");
    }

    // 3️⃣ Admin REJECTS organization
    @PutMapping("/organizations/{id}/reject")
    public ResponseEntity<?> rejectOrganization(@PathVariable Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        organization.setStatus(OrganizationStatus.REJECTED);
        organizationRepository.save(organization);

        return ResponseEntity.ok("Organization rejected");
    }

    // 4️⃣ Admin views uploaded document
    @GetMapping("/organizations/{id}/document")
    public ResponseEntity<Resource> viewOrganizationDocument(@PathVariable Long id) throws Exception {

        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Path filePath = Paths.get(UPLOAD_DIR, org.getDocumentPath());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
