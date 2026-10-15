package com.dits.dits_group.newsletter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "newsletter_subscribers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_newsletter_subscriber_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsletterSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 180
    )
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    @Builder.Default
    private NewsletterStatus status =
            NewsletterStatus.ACTIVE;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime subscribedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ==========================================
    // CRÉATION
    // ==========================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        subscribedAt = now;
        updatedAt = now;

        if (status == null) {
            status =
                    NewsletterStatus.ACTIVE;
        }
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