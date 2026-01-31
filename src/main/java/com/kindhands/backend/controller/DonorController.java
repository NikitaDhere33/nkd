package com.kindhands.backend.controller;

import com.kindhands.backend.entity.User;
import com.kindhands.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*")
public class DonorController {

    private final UserRepository userRepository;

    public DonorController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 REGISTER DONOR
    @PostMapping("/register")
    public ResponseEntity<?> registerDonor(@RequestBody User donor) {

        // role forcefully DONOR
        donor.setRole("DONOR");

        // 🔴 Duplicate email check
        if (userRepository.findByEmail(donor.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Duplicate entry: Email already registered");
        }

        // 🔴 Duplicate mobile check
        if (userRepository.findByMobile(donor.getMobile()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Duplicate entry: Mobile number already registered");
        }

        User savedDonor = userRepository.save(donor);
        return ResponseEntity.ok(savedDonor);
    }

    // 🔹 LOGIN DONOR
    @PostMapping("/login")
    public ResponseEntity<?> loginDonor(@RequestBody User loginData) {

        Optional<User> optionalUser = userRepository.findByEmail(loginData.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email not registered");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(loginData.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid password");
        }

        if (!"DONOR".equals(user.getRole())) {
            return ResponseEntity
                    .badRequest()
                    .body("Not a donor account");
        }

        return ResponseEntity.ok(user);
    }
}
