package com.dits.dits_group.auth.controller;

import com.dits.dits_group.auth.dto.ForgotPasswordRequest;
import com.dits.dits_group.auth.dto.LoginRequest;
import com.dits.dits_group.auth.dto.LoginResponse;
import com.dits.dits_group.auth.dto.RegisterRequest;
import com.dits.dits_group.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dits.dits_group.auth.dto.ResetPasswordRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(
                java.util.Map.of(
                        "message", "Déconnexion réussie"
                )
        );
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {

        authService.register(request);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message",
                        "Inscription réussie. Votre compte est en attente de validation."
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {

        authService.forgotPassword(request);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message",
                        "Si cette adresse correspond à un compte, un lien de réinitialisation a été envoyé par email."
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message",
                        "Votre mot de passe a été réinitialisé avec succès."
                )
        );
    }
}