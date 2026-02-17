package com.kindhands.backend.service;

import com.kindhands.backend.entity.Donate;
import com.kindhands.backend.entity.DonationRequest;
import com.kindhands.backend.enums.RequestStatus;
import com.kindhands.backend.repository.DonationRepository;
import com.kindhands.backend.repository.DonationRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonationService {

    private final DonationRequestRepository requestRepo;
    private final DonationRepository donationRepo;

    public DonationService(DonationRequestRepository requestRepo,
                           DonationRepository donationRepo) {
        this.requestRepo = requestRepo;
        this.donationRepo = donationRepo;
    }

    // ================= ORGANIZATION =================

    // Organization creates donation request
    public DonationRequest createRequest(DonationRequest request) {
        request.setStatus(RequestStatus.OPEN);
        request.setCreatedAt(LocalDateTime.now());
        return requestRepo.save(request);
    }

    // ================= DONOR =================

    // Donor views all OPEN requests
    public List<DonationRequest> getOpenRequests() {
        return requestRepo.findByStatus(RequestStatus.OPEN);
    }

    // Donor donates for a request
    public Donate donate(Donate donate) {

        // save donation
        donate.setDonatedAt(LocalDateTime.now());
        Donate savedDonation = donationRepo.save(donate);

        // update request status
        DonationRequest request = requestRepo
                .findById(donate.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.COMPLETED);
        request.setCompletedAt(LocalDateTime.now());
        requestRepo.save(request);

        return savedDonation;
    }

    // ================= PUBLIC =================

    // Public donation history (only allowed donors)
    public List<Donate> getPublicHistory() {
        return donationRepo.findByPublicHistoryTrue();
    }
}
