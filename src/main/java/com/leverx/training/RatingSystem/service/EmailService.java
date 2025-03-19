package com.leverx.training.RatingSystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationToken(String email, String token){
        String url = "http://localhost:8080/auth/verify?token=" + token;
        String message = "<h2>Hello, user!</h2>"
                + "<p>You just send the request to create the account.</p>"
                + "<p>Click the link below to verify your email:"
                + "<p><a href=\"" + url + "\">Verify email</a></p>";
        String subject = "Verify your email";

        sendEmail(email, message, subject);
    }

    public void sendEmail(String email, String content, String subject){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content);
            javaMailSender.send(message);
            System.out.println("Token was sent to " + email);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email verification token to " + email + ". " + e.getMessage());
        }
    }
}
