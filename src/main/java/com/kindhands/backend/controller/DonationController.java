package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Donate;
import com.kindhands.backend.repository.DonationRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donation")
@CrossOrigin(origins = "*")
public class DonationController {

    private final DonationRepository donationRepository;

    public DonationController(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    // Save donation form data
    @PostMapping("/save")
    public Donate saveDonation(@RequestBody Donate donation) {
        return donationRepository.save(donation);
    }
}
