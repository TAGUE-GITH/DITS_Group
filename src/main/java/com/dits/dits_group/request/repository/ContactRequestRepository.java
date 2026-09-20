package com.dits.dits_group.request.repository;

import com.dits.dits_group.request.entity.ContactRequest;
import com.dits.dits_group.request.entity.RequestStatus;
import com.dits.dits_group.request.entity.RequestType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRequestRepository
        extends JpaRepository<ContactRequest, Long> {

    // ==========================================
    // TOUTES LES DEMANDES
    // ==========================================

    List<ContactRequest> findAllByOrderByCreatedAtDesc();

    // ==========================================
    // FILTRER PAR STATUT
    // ==========================================

    List<ContactRequest>
    findByStatusOrderByCreatedAtDesc(
            RequestStatus status
    );

    // ==========================================
    // FILTRER PAR TYPE
    // ==========================================

    List<ContactRequest>
    findByTypeOrderByCreatedAtDesc(
            RequestType type
    );

    // ==========================================
    // FILTRER PAR TYPE + STATUT
    // ==========================================

    List<ContactRequest>
    findByTypeAndStatusOrderByCreatedAtDesc(
            RequestType type,
            RequestStatus status
    );

    long countByStatus(RequestStatus status);
}