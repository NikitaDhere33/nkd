package com.kindhands.backend.controller;

import com.kindhands.backend.entity.User;
import com.kindhands.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final UserRepository userRepository;

    public DonorController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 REGISTER DONOR
    @PostMapping("/register")
    public ResponseEntity<?> registerDonor(@RequestBody User donor) {
        User savedDonor = userRepository.save(donor);
        return ResponseEntity.ok(savedDonor);
    }

    // 🔹 LOGIN DONOR
    @PostMapping("/login")
    public ResponseEntity<?> loginDonor(@RequestBody User loginData) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(loginData.getEmail())
                        && u.getPassword().equals(loginData.getPassword()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        return ResponseEntity.ok(user);
    }
}
