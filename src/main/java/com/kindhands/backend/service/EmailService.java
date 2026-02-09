package com.kindhands.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("team.kindhands12@gmail.com");
        message.setSubject("KindHands - OTP for Password Reset");
        message.setText(
                "Dear User,\n\n" +
                        "Your OTP for password reset is: " + otp + "\n\n" +
                        "Do not share this OTP with anyone.\n\n" +
                        "Regards,\n" +
                        "KindHands Team"
        );

        mailSender.send(message);
    }

    public void sendEmail(String email, String s, String s1) {
    }
}
