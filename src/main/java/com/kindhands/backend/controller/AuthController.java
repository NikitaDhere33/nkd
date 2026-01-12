package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Organization;
import com.kindhands.backend.entity.OrganizationStatus;
import com.kindhands.backend.entity.User;
import com.kindhands.backend.repository.OrganizationRepository;
import com.kindhands.backend.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public AuthController(UserRepository userRepository,
                          OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already registered"));
        }

        // 🔥 DEFAULT ROLE
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("DONOR");
        }

        userRepository.save(user);

        // 🔥 IMPORTANT: return USER (frontend needs userId)
        return ResponseEntity.ok(user);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {

        User user = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!user.getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid password"));
        }

        // 🔥 ORGANIZATION CHECK
        if ("ORGANIZATION".equalsIgnoreCase(user.getRole())) {

            Organization org = organizationRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Organization profile not found"));

            if (org.getStatus() != OrganizationStatus.APPROVED) {
                return ResponseEntity.status(403)
                        .body(Map.of("message", "Organization not approved"));
            }
        }

        return ResponseEntity.ok(user);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> data) {

        User user = userRepository.findByMobile(data.get("mobile"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        user.setOtp(otp);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "OTP sent"));
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> data) {

        User user = userRepository.findByMobile(data.get("mobile"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!data.get("otp").equals(user.getOtp())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid OTP"));
        }

        return ResponseEntity.ok(Map.of("message", "OTP verified"));
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> data) {

        User user = userRepository.findByMobile(data.get("mobile"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(data.get("newPassword"));
        user.setOtp(null);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }
}
