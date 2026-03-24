package com.kindhands.backend.controller;

import com.kindhands.backend.entity.*;
import com.kindhands.backend.enums.RequestStatus;
import com.kindhands.backend.repository.*;
import com.kindhands.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class DonationRequestController {

    @Autowired
    private DonationRequestRepository repository;

    @Autowired
    private OrganizationRepository orgRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    // ================= 1. HOME PAGE IMPACT TABLE =================
    @GetMapping("/history/public")
    public List<DonationRequest> getPublicHistory() {
        // होम पेजवर फक्त COMPLETED डोनेशन्स पाठवा
        return repository.findByStatusAndIsPublicTrueOrderByCreatedAtDesc(RequestStatus.COMPLETED);
    }

    // ================= 2. ORGANIZATION CREATES NEED =================
    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody DonationRequest request) {
        try {
            request.setStatus(RequestStatus.PENDING);
            request.setCreatedAt(LocalDateTime.now());
            request.setPublic(true);

            if (request.getOrganizationId() != null) {
                orgRepo.findById(request.getOrganizationId()).ifPresent(org -> {
                    request.setOrganizationName(org.getName());
                    request.setOrganizationAddress(org.getAddress());
                });
            }
            return ResponseEntity.ok(repository.save(request));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // ================= 3. DONOR ACCEPTS (Status -> COMPLETED) =================
    @PutMapping("/{requestId}/donor-accept/{donorId}")
    public ResponseEntity<?> donorAcceptRequirement(@PathVariable Long requestId, @PathVariable Long donorId) {
        return repository.findById(requestId).map(request -> {

            // डोनरचे डिटेल्स मिळवा
            User donor = userRepo.findById(donorId).orElse(null);
            String dName = (donor != null) ? donor.getName() : "A Kind Donor";
            String dPhone = (donor != null) ? donor.getMobile() : "N/A";

            // स्टेटस COMPLETED करा जेणेकरून ते होम पेजवर दिसेल
            request.setStatus(RequestStatus.COMPLETED);
            request.setDonorName(dName);
            request.setCompletedAt(LocalDateTime.now());
            repository.save(request);

            // ईमेल पाठवा
            try {
                Organization org = orgRepo.findById(request.getOrganizationId()).orElse(null);
                if (org != null) {
                    String subject = "Kind Hands: Your Requirement Accepted!";
                    String body = "Hello " + org.getName() + ",\n\n" +
                            "A donor (" + dName + ") has accepted your request for " + request.getItemNeeded() + ".\n" +
                            "Contact: " + dPhone + "\n\nTeam Kind Hands";
                    emailService.sendEmail(org.getEmail(), subject, body);
                }
            } catch (Exception e) { e.printStackTrace(); }

            return ResponseEntity.ok("Donation accepted and status updated.");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pending")
    public List<DonationRequest> getPendingRequests() {
        return repository.findByStatus(RequestStatus.PENDING);
    }
}