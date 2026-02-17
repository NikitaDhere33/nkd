package com.kindhands.backend.controller;

import com.kindhands.backend.entity.DonationRequest;
import com.kindhands.backend.enums.RequestStatus;
import com.kindhands.backend.repository.DonationRequestRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donation-requests")
@CrossOrigin(origins = "*")
public class DonationRequestController {

    private final DonationRequestRepository requestRepository;

    public DonationRequestController(DonationRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    // Organization creates request
    @PostMapping("/create")
    public DonationRequest createRequest(@RequestBody DonationRequest request) {
        return requestRepository.save(request);
    }

    // Get all OPEN requests (for donors dashboard)
    @GetMapping("/open")
    public List<DonationRequest> getOpenRequests() {
        return requestRepository.findByStatus(RequestStatus.OPEN);
    }

    // Mark request completed
    @PutMapping("/{id}/complete")
    public DonationRequest completeRequest(@PathVariable Long id) {
        DonationRequest req = requestRepository.findById(id).orElseThrow();
        req.setStatus(RequestStatus.COMPLETED);
        return requestRepository.save(req);
    }
}
