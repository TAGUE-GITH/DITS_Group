package com.dits.dits_group.partner.repository;

import com.dits.dits_group.partner.entity.Partner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartnerRepository
        extends JpaRepository<Partner, Long> {

    // ==========================================
    // PUBLIC : PARTENAIRES PUBLIÉS
    // ==========================================

    List<Partner> findByPublishedTrueOrderByCreatedAtDesc();

    // ==========================================
    // PUBLIC : DÉTAIL PUBLIÉ
    // ==========================================

    Optional<Partner> findByIdAndPublishedTrue(
            Long id
    );

    // ==========================================
    // ADMIN : LISTE COMPLÈTE
    // ==========================================

    List<Partner> findAllByOrderByCreatedAtDesc();

    // ==========================================
    // ADMIN : FILTRE PUBLICATION
    // ==========================================

    List<Partner> findByPublishedOrderByCreatedAtDesc(
            boolean published
    );

    // ==========================================
    // RECHERCHE PAR NOM
    // ==========================================

    Optional<Partner> findByNameIgnoreCase(
            String name
    );

    // ==========================================
    // VÉRIFIER SI LE NOM EXISTE
    // ==========================================

    boolean existsByNameIgnoreCase(
            String name
    );

    // ==========================================
    // COMPTEURS
    // ==========================================

    long countByPublished(
            boolean published
    );
}