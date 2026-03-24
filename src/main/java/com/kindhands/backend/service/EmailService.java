package com.kindhands.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Common method to send emails with error handling
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            // TUMCHA EMAIL ITHE TAKA
            message.setFrom("team.kindhands12@gmail.com");

            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            // Error handling: prevents app crash if email fails
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    // १. 'sendSimpleEmail' (Requirement accept kelyavar organization la mail pathvnyasathi)
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        sendEmail(toEmail, subject, body);
    }

    // २. 'sendOtpEmail' (Login/Registration OTP sathi)
    public void sendOtpEmail(String toEmail, String otp) {
        String subject = "Kind Hands - Verification OTP";
        String body = "Hello,\n\nYour OTP for verification is: " + otp +
                "\n\nPlease use this to complete your process. Do not share it with anyone.\n\n" +
                "Team Kind Hands";
        sendEmail(toEmail, subject, body);
    }
}