package com.dits.dits_group.recruitment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // TITRE
    // ==========================================

    @Column(
            nullable = false,
            length = 180
    )
    private String title;

    // ==========================================
    // DESCRIPTION
    // ==========================================

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    // ==========================================
    // LOCALISATION
    // ==========================================

    @Column(
            nullable = false,
            length = 150
    )
    private String location;

    // ==========================================
    // TYPE DE CONTRAT
    // ==========================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 50
    )
    private ContractType contractType;

    // ==========================================
    // PRÉREQUIS / COMPÉTENCES
    // ==========================================

    @Column(
            columnDefinition = "TEXT"
    )
    private String requirements;

    // ==========================================
    // DATE LIMITE DE CANDIDATURE
    // ==========================================

    private LocalDate closingDate;

    // ==========================================
    // PUBLICATION
    // ==========================================

    @Builder.Default
    @Column(nullable = false)
    private boolean published = false;

    // ==========================================
    // DATES TECHNIQUES
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