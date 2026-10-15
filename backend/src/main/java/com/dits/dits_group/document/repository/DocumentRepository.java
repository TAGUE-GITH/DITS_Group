package com.dits.dits_group.document.repository;

import com.dits.dits_group.document.entity.Document;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository
        extends JpaRepository<Document, Long> {

    // ==========================================
    // DOCUMENTS D'UN UTILISATEUR PAR SON ID
    // ==========================================

    List<Document> findByUserIdOrderByUploadedAtDesc(
            Long userId
    );

    // ==========================================
    // DOCUMENTS D'UN UTILISATEUR PAR SON EMAIL
    // Pratique pour le client connecté
    // ==========================================

    List<Document> findByUserEmailOrderByUploadedAtDesc(
            String email
    );

    // ==========================================
    // TOUS LES DOCUMENTS POUR L'ADMIN
    // ==========================================

    List<Document> findAllByOrderByUploadedAtDesc();
}