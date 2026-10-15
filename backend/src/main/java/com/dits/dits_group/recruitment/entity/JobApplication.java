package com.dits.dits_group.recruitment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // INFORMATIONS DU CANDIDAT
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

    @Column(
            nullable = false,
            length = 180
    )
    private String email;

    @Column(length = 30)
    private String phone;

    // ==========================================
    // MESSAGE / MOTIVATION
    // ==========================================

    @Column(columnDefinition = "TEXT")
    private String message;

    // ==========================================
    // CV
    // ==========================================

    @Column(
            nullable = false,
            length = 255
    )
    private String cvOriginalFileName;

    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String cvStoredFileName;

    @Column(
            nullable = false,
            length = 500
    )
    private String cvUrl;

    @Column(length = 150)
    private String cvContentType;

    private Long cvFileSize;

    // ==========================================
    // STATUT
    // ==========================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 50
    )
    @Builder.Default
    private ApplicationStatus status =
            ApplicationStatus.PENDING;

    // ==========================================
    // NOTE ADMINISTRATEUR
    // ==========================================

    @Column(columnDefinition = "TEXT")
    private String adminNote;

    // ==========================================
    // DATE DE CANDIDATURE
    // ==========================================

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ==========================================
    // OFFRE ASSOCIÉE
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "job_offer_id",
            nullable = false
    )
    private JobOffer jobOffer;

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
                    ApplicationStatus.PENDING;
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