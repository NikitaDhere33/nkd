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

    // CREATE
    @PostMapping
    public Request create(@RequestBody Request request) {
        return requestRepository.save(request);
    }

    // READ ALL
    @GetMapping
    public List<Request> getAll() {
        return requestRepository.findAll();
    }
}
