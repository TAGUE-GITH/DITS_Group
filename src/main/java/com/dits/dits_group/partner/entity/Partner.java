package com.dits.dits_group.partner.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "partners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 180
    )
    private String name;

    @Column(
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            length = 500
    )
    private String logoUrl;

    @Column(
            length = 500
    )
    private String websiteUrl;

    @Builder.Default
    @Column(nullable = false)
    private boolean published = false;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ==========================================
    // CRÉATION
    // ==========================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    // ==========================================
    // MODIFICATION
    // ==========================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}