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

    // ================= REGISTER (NO OTP) =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        if (userRepository.existsByMobile(user.getMobile())) {
            return ResponseEntity.badRequest().body("Mobile already exists");
        }

        userRepository.save(user);
        return ResponseEntity.ok("Registered successfully");
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {

        User user = userRepository.findByEmail(data.get("email"))
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!user.getPassword().equals(data.get("password"))) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        return ResponseEntity.ok(user);
    }

    // ================= FORGOT PASSWORD =================
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

    // ================= RESET PASSWORD =================
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

    // ================= OTP GENERATOR =================
    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
