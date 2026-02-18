package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Organization;
import com.kindhands.backend.entity.OrganizationStatus;
import com.kindhands.backend.repository.OrganizationRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin(origins = "*")
public class OrganizationController {

    private final OrganizationRepository repo;

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "orgs";

    public OrganizationController(OrganizationRepository repo) {
        this.repo = repo;
    }

    // ================= REGISTER =================
    // ================= REGISTER =================
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String contact,
            @RequestParam String type,
            @RequestParam String address,
            @RequestParam String pincode,
            @RequestParam("document") MultipartFile document
    ) {
        try {
            // 🔒 Duplicate email
            if (repo.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("Email already registered");
            }

            // 🔒 Duplicate contact
            if (repo.findByContact(contact).isPresent()) {
                return ResponseEntity.badRequest().body("Contact already registered");
            }

            // 📁 Upload document
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + document.getOriginalFilename();
            document.transferTo(new File(dir, fileName));

            // 🏢 Save organization
            Organization org = new Organization();
            org.setName(name);
            org.setEmail(email);
            org.setPassword(password);
            org.setContact(contact);
            org.setType(type);
            org.setAddress(address);
            org.setPincode(pincode);
            org.setDocumentPath(fileName);
            org.setStatus(OrganizationStatus.PENDING);

            repo.save(org);

            return ResponseEntity.ok("Organization registered. Waiting for admin approval");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Organization registration failed");
        }
    }


    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {

        Organization org = repo.findByEmail(data.get("email")).orElse(null);

        if (org == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Organization not found");
        }

        if (!org.getPassword().equals(data.get("password"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        if (org.getStatus() != OrganizationStatus.APPROVED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Organization not approved");
        }

        return ResponseEntity.ok(org);
    }

    // ================= DONOR =================
    @GetMapping("/donor/approved")
    public List<Organization> approvedOrganizations() {
        return repo.findByStatus(OrganizationStatus.APPROVED);
    }

    // ================= ADMIN =================
    @GetMapping("/admin/pending")
    public List<Organization> pendingOrganizations() {
        return repo.findByStatus(OrganizationStatus.PENDING);
    }

    @PutMapping("/admin/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        Organization org = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        org.setStatus(OrganizationStatus.APPROVED);
        repo.save(org);
        return ResponseEntity.ok("Organization approved");
    }

    @PutMapping("/admin/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        Organization org = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        org.setStatus(OrganizationStatus.REJECTED);
        repo.save(org);
        return ResponseEntity.ok("Organization rejected");
    }

    // ================= VIEW DOCUMENT =================
    @GetMapping("/admin/document/{id}")
    public ResponseEntity<Resource> viewDocument(@PathVariable Long id) throws Exception {

        Organization org = repo.findById(id)
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
