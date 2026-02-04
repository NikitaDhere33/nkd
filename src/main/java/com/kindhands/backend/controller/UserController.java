package com.kindhands.backend.controller;

import com.kindhands.backend.entity.User;
import com.kindhands.backend.repository.UserRepository;
import com.kindhands.backend.service.EmailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserController(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // ===================== REGISTER =====================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // 🔒 Duplicate email check
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Duplicate entry: Email already exists");
        }

        // 🔒 Duplicate mobile check
        if (userRepository.existsByMobile(user.getMobile())) {
            return ResponseEntity.badRequest().body("Duplicate entry: Mobile already exists");
        }

        // Generate OTP
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setEmailVerified(false);

        userRepository.save(user);

        // Send OTP Email
        emailService.sendEmail(
                user.getEmail(),
                "KindHands Email Verification",
                "Your OTP is: " + otp + "\nValid for 5 minutes"
        );

        return ResponseEntity.ok("Registered successfully. Please verify OTP sent to email.");
    }

    // ===================== VERIFY OTP =====================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null) {
            return ResponseEntity.badRequest().body("OTP not generated");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP expired");
        }

        if (!user.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        user.setEmailVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Email verified successfully");
    }

    // ===================== LOGIN =====================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {

        User user = userRepository.findByEmail(data.get("email"))
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!user.getPassword().equals(data.get("password"))) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        if (!user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("Email not verified");
        }

        return ResponseEntity.ok(user);
    }

    // ===================== FORGOT PASSWORD =====================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        emailService.sendEmail(
                email,
                "KindHands Password Reset OTP",
                "Your OTP is: " + otp + "\nValid for 5 minutes"
        );

        return ResponseEntity.ok("OTP sent to email");
    }

    // ===================== RESET PASSWORD =====================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null) {
            return ResponseEntity.badRequest().body("OTP not generated");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP expired");
        }

        if (!user.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        user.setPassword(newPassword);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }

    // ===================== OTP GENERATOR =====================
    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
