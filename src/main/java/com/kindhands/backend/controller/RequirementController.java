package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Requirement;
import com.kindhands.backend.repository.RequirementRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirements")
@CrossOrigin(origins = "*")
public class RequirementController {

    private final RequirementRepository requirementRepository;

    public RequirementController(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    // CREATE
    @PostMapping
    public Requirement create(@RequestBody Requirement requirement) {
        return requirementRepository.save(requirement);
    }

    // READ ALL
    @GetMapping
    public List<Requirement> getAll() {
        return requirementRepository.findAll();
    }
}
