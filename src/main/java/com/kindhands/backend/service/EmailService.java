package com.kindhands.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // ✅ OTP EMAIL
    public void sendOtpEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(fromEmail);
            message.setSubject("KindHands - OTP for Password Reset");
            message.setText(
                    "Dear User,\n\n" +
                            "Your OTP for password reset is: " + otp + "\n\n" +
                            "Do not share this OTP with anyone.\n\n" +
                            "Regards,\n" +
                            "KindHands Team"
            );

            mailSender.send(message);
            System.out.println("✅ OTP email sent to: " + to);

        } catch (Exception e) {
            System.err.println(" Failed to send OTP email to " + to);
            e.printStackTrace();
            throw new RuntimeException("Email sending failed");
        }
    }

    public void sendEmail(String email, String s, String s1) {

    }
}
