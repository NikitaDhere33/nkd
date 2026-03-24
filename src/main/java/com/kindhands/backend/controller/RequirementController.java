package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Requirement;
import com.kindhands.backend.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // हा सर्व अ‍ॅनोटेशन्स इंपोर्ट करेल

import java.util.List;

@RestController
@RequestMapping("/api/requirements")
public class RequirementController {
    @Autowired private RequirementService requirementService;

    // Organization requirement post karel
    @PostMapping("/create/{orgId}")
    public ResponseEntity<?> createRequirement(@RequestBody Requirement req, @PathVariable Long orgId) {
        return ResponseEntity.ok(requirementService.saveRequirement(req, orgId));
    }

    // Donor la saglya active requirements distil
    @GetMapping("/all-active")
    public List<Requirement> getActiveRequirements() {
        return requirementService.getActiveRequirements();
    }

    // Donor requirement accept karel ani email jail
    @PutMapping("/{reqId}/accept/{donorId}")
    public ResponseEntity<?> acceptRequirement(@PathVariable Long reqId, @PathVariable Long donorId) {
        requirementService.acceptAndNotify(reqId, donorId);
        return ResponseEntity.ok("Requirement accepted. Email sent to organization.");
    }
}