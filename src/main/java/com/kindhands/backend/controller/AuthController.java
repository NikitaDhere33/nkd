package com.kindhands.backend.controller;

import com.kindhands.backend.entity.Organization;
import com.kindhands.backend.entity.OrganizationStatus;
import com.kindhands.backend.entity.User;
import com.kindhands.backend.repository.OrganizationRepository;
import com.kindhands.backend.repository.UserRepository;
import com.kindhands.backend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final EmailService emailService;

    public AuthController(
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.emailService = emailService;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already registered"));
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("DONOR");
        }

        userRepository.save(user);
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

        // Organization approval check
        if ("ORGANIZATION".equalsIgnoreCase(user.getRole())) {
            Organization org = organizationRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));

            if (org.getStatus() != OrganizationStatus.APPROVED) {
                return ResponseEntity.status(403)
                        .body(Map.of("message", "Organization not approved"));
            }
        }

        return ResponseEntity.ok(user);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        User user = null;

        // 🔹 1) First check USER table (DONOR / ORGANIZATION user)
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            // 🔹 2) If not found, check ORGANIZATION table
            Optional<Organization> orgOpt = organizationRepository.findByEmail(email);

            if (orgOpt.isPresent()) {
                Organization org = orgOpt.get();

                // 🔹 map organization → user
                user = userRepository.findById(org.getUserId())
                        .orElseThrow(() -> new RuntimeException("Linked user not found"));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email not found"));
            }
        }

        // 🔐 Generate OTP
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        user.setOtp(otp);
        userRepository.save(user);

        // 📧 Send OTP Email
        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok(Map.of("message", "OTP sent to email"));
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid OTP"));
        }

        return ResponseEntity.ok(Map.of("message", "OTP verified"));
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "OTP not verified"));
        }

        user.setPassword(newPassword);
        user.setOtp(null); // clear OTP
        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("message", "Password reset successful")
        );
    }
}
