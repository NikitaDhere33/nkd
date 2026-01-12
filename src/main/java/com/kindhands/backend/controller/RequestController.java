package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Request;
import com.kindhands.backend.entity.RequestStatus;
import com.kindhands.backend.repository.RequestRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin
public class RequestController {

    private final RequestRepository repo;

    public RequestController(RequestRepository repo) {
        this.repo = repo;
    }

    // 1️⃣ Organization creates request
    @PostMapping("/create")
    public Request create(@RequestBody Request request) {
        request.setStatus(RequestStatus.OPEN);
        return repo.save(request);
    }

    // 2️⃣ Donor sees open requests
    @GetMapping("/open")
    public List<Request> openRequests() {
        return repo.findByStatus(RequestStatus.OPEN);
    }

    // 3️⃣ Donor accepts request
    @PutMapping("/{id}/accept/{donorId}")
    public Request accept(@PathVariable Long id, @PathVariable Long donorId) {

        Request request = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.ACCEPTED);
        request.setDonorId(donorId);

        return repo.save(request);
    }

    // 4️⃣ Donor rejects request
    @PutMapping("/{id}/reject")
    public Request reject(@PathVariable Long id) {

        Request request = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);
        return repo.save(request);
    }

    // 5️⃣ Organization dashboard - its own requests
    @GetMapping("/organization/{orgId}")
    public List<Request> orgRequests(@PathVariable Long orgId) {
        return repo.findByOrganizationId(orgId);
    }

    // 6️⃣ Donor confirms delivery
    @PutMapping("/{id}/delivered")
    public Request markDelivered(@PathVariable Long id) {

        Request request = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.ACCEPTED) {
            throw new RuntimeException("Request not accepted yet");
        }

        request.setStatus(RequestStatus.DELIVERED);
        return repo.save(request);
    }

    // 7️⃣ Organization confirms received (FINAL STEP)
    @PutMapping("/{id}/complete")
    public Request complete(@PathVariable Long id) {

        Request request = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.DELIVERED) {
            throw new RuntimeException("Request not delivered yet");
        }

        request.setStatus(RequestStatus.COMPLETED);
        return repo.save(request);
    }
}
