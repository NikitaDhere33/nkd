package com.kindhands.backend.service;

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

    // Organization creates request
    public DonationRequest createRequest(DonationRequest request) {
        return requestRepo.save(request);
    }

    // Donor sees open requests
    public List<DonationRequest> getOpenRequests() {
        return requestRepo.findByStatus(RequestStatus.OPEN);
    }

    // Donor donates
    public Donation donate(Donation donation) {
        Donation saved = donationRepo.save(donation);

        DonationRequest req = requestRepo.findById(donation.getRequestId()).orElseThrow();
        req.setStatus(RequestStatus.COMPLETED);
        req.setCompletedAt(LocalDateTime.now());
        requestRepo.save(req);

        return saved;
    }

    // Public history
    public List<Donation> getPublicHistory() {
        return donationRepo.findByPublicHistoryTrue();
    }
}
