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

    public DonationRequest createRequest(DonationRequest request) {
        request.setStatus(RequestStatus.OPEN); // No more error here!
        request.setCreatedAt(LocalDateTime.now());
        return requestRepo.save(request);
    }

    public List<DonationRequest> getOpenRequests() {
        return requestRepo.findByStatus(RequestStatus.OPEN); // No more error here!
    }

    public Donate donate(Donate donate) {
        donate.setDonatedAt(LocalDateTime.now());
        Donate savedDonation = donationRepo.save(donate);

        // NOTE: Make sure donate.getOrganizationId() actually
        // contains the ID of the DonationRequest!
        DonationRequest request = requestRepo
                .findById(donate.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.COMPLETED);
        request.setCompletedAt(LocalDateTime.now());
        requestRepo.save(request);

        return savedDonation;
    }

    public List<Donate> getPublicHistory() {
        return donationRepo.findByPublicHistoryTrue();
    }
}