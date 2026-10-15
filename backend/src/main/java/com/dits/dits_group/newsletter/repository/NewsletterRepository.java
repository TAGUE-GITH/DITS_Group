package com.dits.dits_group.newsletter.repository;

import com.dits.dits_group.newsletter.entity.NewsletterStatus;
import com.dits.dits_group.newsletter.entity.NewsletterSubscriber;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsletterRepository
        extends JpaRepository<NewsletterSubscriber, Long> {

    // ==========================================
    // RECHERCHE PAR EMAIL
    // ==========================================

    Optional<NewsletterSubscriber> findByEmailIgnoreCase(
            String email
    );

    // ==========================================
    // VÉRIFIER SI UN EMAIL EXISTE
    // ==========================================

    boolean existsByEmailIgnoreCase(
            String email
    );

    // ==========================================
    // LISTE PAR STATUT
    // ==========================================

    List<NewsletterSubscriber>
    findByStatusOrderBySubscribedAtDesc(
            NewsletterStatus status
    );

    // ==========================================
    // LISTE COMPLÈTE
    // ==========================================

    List<NewsletterSubscriber>
    findAllByOrderBySubscribedAtDesc();

    // ==========================================
    // COMPTER PAR STATUT
    // ==========================================

    long countByStatus(
            NewsletterStatus status
    );
}