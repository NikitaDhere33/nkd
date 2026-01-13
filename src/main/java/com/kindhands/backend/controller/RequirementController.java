package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Requirement;
import com.kindhands.backend.service.RequirementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requirements")
@CrossOrigin(origins = "*")
public class RequirementController {

    private final RequirementService service;

    public RequirementController(RequirementService service) {
        this.service = service;
    }

    // 🔹 Organization posts requirement
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Requirement req) {
        Requirement saved = service.createRequirement(req);
        return ResponseEntity.ok(saved);
    }

    // 🔹 Organization dashboard – its own requirements
    @GetMapping("/organization/{orgId}")
    public List<Requirement> orgRequirements(@PathVariable Long orgId) {
        return service.getOrgRequirements(orgId);
    }

    // 🔹 Donor – see all open requirements
    @GetMapping("/open")
    public List<Requirement> openRequirements() {
        return service.getAllOpenRequirements();
    }
}
