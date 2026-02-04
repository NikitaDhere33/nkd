package com.kindhands.backend.service;

import com.kindhands.backend.entity.User;
import com.kindhands.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // 🔹 OTP Generate & Send
    public void sendOtp(User user) {

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "KindHands OTP Verification",
                "Your OTP is: " + otp + " (valid for 5 minutes)"
        );
    }
}
