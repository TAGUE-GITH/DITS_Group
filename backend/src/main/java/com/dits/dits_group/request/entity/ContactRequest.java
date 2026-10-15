package com.dits.dits_group.request.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // IDENTITÉ
    // ==========================================

    @Column(
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            nullable = false,
            length = 100
    )
    private String lastName;

    // ==========================================
    // CONTACT
    // ==========================================

    @Column(
            nullable = false,
            length = 180
    )
    private String email;

    @Column(length = 30)
    private String phone;

    // ==========================================
    // ENTREPRISE
    // ==========================================

    @Column(length = 180)
    private String company;

    // ==========================================
    // TYPE DE DEMANDE
    // ==========================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 50
    )
    private RequestType type;

    // ==========================================
    // SUJET
    // ==========================================

    @Column(
            nullable = false,
            length = 200
    )
    private String subject;

    // ==========================================
    // MESSAGE
    // ==========================================

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String message;

    // ==========================================
    // STATUT
    // ==========================================

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 50
    )
    private RequestStatus status =
            RequestStatus.PENDING;

    // ==========================================
    // NOTE INTERNE ADMIN
    // ==========================================

    @Column(columnDefinition = "TEXT")
    private String adminNote;

    // ==========================================
    // DATES
    // ==========================================

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ==========================================
    // AVANT INSERTION
    // ==========================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status =
                    RequestStatus.PENDING;
        }
    }

    // ==========================================
    // AVANT MODIFICATION
    // ==========================================

    @PreUpdate
    protected void onUpdate() {
        updatedAt =
                LocalDateTime.now();
    }
}