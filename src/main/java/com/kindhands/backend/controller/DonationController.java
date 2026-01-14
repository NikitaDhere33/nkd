package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Donate;
import com.kindhands.backend.repository.DonationRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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



    @GetMapping("/all")
    public List<Donate> getAllDonations() {
        return donationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Donate getDonationById(@PathVariable Long id) {
        return donationRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteDonation(@PathVariable Long id) {
        donationRepository.deleteById(id);
    }


}
