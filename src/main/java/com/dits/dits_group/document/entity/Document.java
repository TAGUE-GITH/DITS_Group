package com.dits.dits_group.document.entity;

import com.dits.dits_group.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // TITRE DU DOCUMENT
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
            columnDefinition = "TEXT"
    )
    private String description;

    // ==========================================
    // NOM ORIGINAL DU FICHIER
    // Exemple : facture-aout-2026.pdf
    // ==========================================

    @Column(
            nullable = false,
            length = 255
    )
    private String originalFileName;

    // ==========================================
    // NOM UTILISÉ POUR LE STOCKAGE
    // Exemple :
    // 6c59a4c1-facture-aout-2026.pdf
    // ==========================================

    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String storedFileName;

    // ==========================================
    // URL / CHEMIN DU FICHIER
    // ==========================================

    @Column(
            nullable = false,
            length = 500
    )
    private String fileUrl;

    // ==========================================
    // TYPE MIME
    // Exemple :
    // application/pdf
    // image/png
    // ==========================================

    @Column(length = 150)
    private String contentType;

    // ==========================================
    // TAILLE EN OCTETS
    // ==========================================

    private Long fileSize;

    // ==========================================
    // DATE D'AJOUT
    // ==========================================

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime uploadedAt;

    // ==========================================
    // CLIENT PROPRIÉTAIRE DU DOCUMENT
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    // ==========================================
    // AVANT INSERTION
    // ==========================================

    @PrePersist
    protected void onCreate() {

        if (uploadedAt == null) {
            uploadedAt =
                    LocalDateTime.now();
        }
    }
}