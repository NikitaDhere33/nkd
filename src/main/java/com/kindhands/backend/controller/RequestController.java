package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Request;
import com.kindhands.backend.repository.RequestRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestRepository requestRepository;

    public RequestController(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    // 1. Organization creates request
    @PostMapping
    public Request create(@RequestBody Request request) {
        request.setStatus("PENDING");
        return requestRepository.save(request);
    }

    // 2. Donor dashboard - see all pending requests
    @GetMapping("/pending")
    public List<Request> getPendingRequests() {
        return requestRepository.findByStatus("PENDING");
    }

    // 3. View details (single request)
    @GetMapping("/{id}")
    public Request getById(@PathVariable Long id) {
        return requestRepository.findById(id).orElseThrow();
    }

    // 4. Approve
    @PutMapping("/{id}/approve")
    public Request approve(@PathVariable Long id) {
        Request r = requestRepository.findById(id).orElseThrow();
        r.setStatus("APPROVED");
        return requestRepository.save(r);
    }

    // 5. Reject
    @PutMapping("/{id}/reject")
    public Request reject(@PathVariable Long id) {
        Request r = requestRepository.findById(id).orElseThrow();
        r.setStatus("REJECTED");
        return requestRepository.save(r);
    }
}

