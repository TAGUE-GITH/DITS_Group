package com.dits.dits_group.recruitment.repository;

import com.dits.dits_group.recruitment.entity.ApplicationStatus;
import com.dits.dits_group.recruitment.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // ==========================================
    // ADMIN : TOUTES LES CANDIDATURES
    // ==========================================

    List<JobApplication>
    findAllByOrderByCreatedAtDesc();

    // ==========================================
    // ADMIN : CANDIDATURES PAR STATUT
    // ==========================================

    List<JobApplication>
    findByStatusOrderByCreatedAtDesc(
            ApplicationStatus status
    );

    // ==========================================
    // ADMIN : CANDIDATURES PAR OFFRE
    // ==========================================

    List<JobApplication>
    findByJobOfferIdOrderByCreatedAtDesc(
            Long jobOfferId
    );

    // ==========================================
    // ADMIN : OFFRE + STATUT
    // ==========================================

    List<JobApplication>
    findByJobOfferIdAndStatusOrderByCreatedAtDesc(
            Long jobOfferId,
            ApplicationStatus status
    );

    // ==========================================
    // NOMBRE DE CANDIDATURES POUR UNE OFFRE
    // ==========================================

    long countByJobOfferId(
            Long jobOfferId
    );

    long countByStatus(ApplicationStatus status);
}