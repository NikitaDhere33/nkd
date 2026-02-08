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

        // Default role
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("DONOR");
        }

        userRepository.save(user);
        return ResponseEntity.ok(user); // frontend ला userId हवा
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
            Organization org = organizationRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));

            if (org.getStatus() != OrganizationStatus.APPROVED) {
                return ResponseEntity.status(403)
                        .body(Map.of("message", "Organization not approved"));
            }
        }

        return ResponseEntity.ok(user);
    }

    // ================= FORGOT PASSWORD (EMAIL OTP) =================
    // 🔁 HERE EMAIL REPLACED TO @RequestParam
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        user.setOtp(otp);
        userRepository.save(user);

        // 🔴 Email sending future madhe add karta yeil
        System.out.println("OTP for " + email + " = " + otp);

        return ResponseEntity.ok(Map.of("message", "OTP sent to email"));
    }

    // ================= VERIFY OTP =================
    // 🔁 HERE ALSO @RequestParam
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otp.equals(user.getOtp())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid OTP"));
        }

        return ResponseEntity.ok(Map.of("message", "OTP verified"));
    }

    // ================= RESET PASSWORD =================
    // 🔁 HERE ALSO @RequestParam
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);
        user.setOtp(null);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }
}
