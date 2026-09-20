package com.dits.dits_group.article.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // TITRE
    // ==========================================

    @Column(
            nullable = false,
            length = 200
    )
    private String title;

    // ==========================================
    // RÉSUMÉ
    // ==========================================

    @Column(
            nullable = false,
            length = 500
    )
    private String summary;

    // ==========================================
    // CONTENU COMPLET
    // ==========================================

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    // ==========================================
    // IMAGE
    // ==========================================

    @Column(length = 500)
    private String imageUrl;

    // ==========================================
    // AUTEUR
    // ==========================================

    @Column(
            nullable = false,
            length = 150
    )
    private String author;

    // ==========================================
    // DATE DE PUBLICATION
    // ==========================================

    private LocalDateTime publicationDate;

    // ==========================================
    // STATUT
    // false = brouillon
    // true = publié
    // ==========================================

    @Builder.Default
    @Column(nullable = false)
    private boolean published = false;

    // ==========================================
    // DATE DE CRÉATION
    // ==========================================

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    // ==========================================
    // DERNIÈRE MODIFICATION
    // ==========================================

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

        /*
         * Si l'article est directement publié
         * et qu'aucune date n'a été fournie,
         * on utilise la date actuelle.
         */
        if (
                published &&
                        publicationDate == null
        ) {
            publicationDate = now;
        }
    }

    // ==========================================
    // AVANT MODIFICATION
    // ==========================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();

        /*
         * Si un brouillon devient publié,
         * on renseigne automatiquement
         * sa date de publication.
         */
        if (
                published &&
                        publicationDate == null
        ) {
            publicationDate =
                    LocalDateTime.now();
        }
    }
}