package com.dits.dits_group.mail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(
            String email,
            String token
    ) {

        String resetLink =
                frontendUrl
                        + "/reset-password?token="
                        + token;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(email);

        message.setSubject(
                "Réinitialisation de votre mot de passe - DITS Group"
        );

        message.setText(
                "Bonjour,\n\n"
                        + "Vous avez demandé la réinitialisation de votre mot de passe DITS Group.\n\n"
                        + "Cliquez sur le lien suivant pour choisir un nouveau mot de passe :\n\n"
                        + resetLink
                        + "\n\n"
                        + "Ce lien est valable pendant 30 minutes.\n\n"
                        + "Si vous n'êtes pas à l'origine de cette demande, ignorez simplement cet email.\n\n"
                        + "DITS Group"
        );

        mailSender.send(message);
    }
}