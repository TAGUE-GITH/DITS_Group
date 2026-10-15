package com.dits.dits_group.admin.controller;

import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.entity.UserStatus;
import com.dits.dits_group.user.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(
            UserService userService
    ) {
        this.userService = userService;
    }

    // =========================
    // TEST ADMIN
    // =========================

    @GetMapping("/test")
    public ResponseEntity<String> testAdminAccess() {

        return ResponseEntity.ok(
                "Accès administrateur autorisé"
        );
    }

    // =========================
    // TOUS LES UTILISATEURS
    // =========================

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userService.findAll()
        );
    }

    // =========================
    // UTILISATEURS EN ATTENTE
    // =========================

    @GetMapping("/users/pending")
    public ResponseEntity<List<User>> getPendingUsers() {

        return ResponseEntity.ok(
                userService.findByStatus(
                        UserStatus.PENDING
                )
        );
    }

    // =========================
    // UTILISATEURS ACTIFS
    // =========================

    @GetMapping("/users/active")
    public ResponseEntity<List<User>> getActiveUsers() {

        return ResponseEntity.ok(
                userService.findByStatus(
                        UserStatus.ACTIVE
                )
        );
    }

    // =========================
    // UTILISATEURS DÉSACTIVÉS
    // =========================

    @GetMapping("/users/disabled")
    public ResponseEntity<List<User>> getDisabledUsers() {

        return ResponseEntity.ok(
                userService.findByStatus(
                        UserStatus.DISABLED
                )
        );
    }

    // =========================
    // ACTIVER / RÉACTIVER
    // =========================

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<?> activateUser(
            @PathVariable Long id
    ) {

        User user =
                userService.activateUser(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Compte utilisateur activé avec succès.",
                        "id",
                        user.getId(),
                        "email",
                        user.getEmail(),
                        "status",
                        user.getStatus()
                )
        );
    }

    // =========================
    // DÉSACTIVER
    // =========================

    @PutMapping("/users/{id}/disable")
    public ResponseEntity<?> disableUser(
            @PathVariable Long id
    ) {

        User user =
                userService.disableUser(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Compte utilisateur désactivé avec succès.",
                        "id",
                        user.getId(),
                        "email",
                        user.getEmail(),
                        "status",
                        user.getStatus()
                )
        );
    }

    // =========================
    // SUPPRIMER
    // =========================

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Utilisateur supprimé avec succès."
                )
        );
    }
}