package com.dits.dits_group.request.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.request.dto.ContactRequestAdminUpdateRequest;
import com.dits.dits_group.request.dto.ContactRequestCreateRequest;
import com.dits.dits_group.request.dto.ContactRequestResponse;
import com.dits.dits_group.request.entity.ContactRequest;
import com.dits.dits_group.request.entity.RequestStatus;
import com.dits.dits_group.request.entity.RequestType;
import com.dits.dits_group.request.mapper.ContactRequestMapper;
import com.dits.dits_group.request.repository.ContactRequestRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;
    private final ContactRequestMapper contactRequestMapper;

    public ContactRequestService(
            ContactRequestRepository contactRequestRepository,
            ContactRequestMapper contactRequestMapper
    ) {
        this.contactRequestRepository =
                contactRequestRepository;

        this.contactRequestMapper =
                contactRequestMapper;
    }

    // ==========================================
    // PUBLIC : CRÉER UNE DEMANDE
    // ==========================================

    @Transactional
    public ContactRequestResponse create(
            ContactRequestCreateRequest request
    ) {

        ContactRequest contactRequest =
                contactRequestMapper.toEntity(
                        request
                );

        ContactRequest savedRequest =
                contactRequestRepository.save(
                        contactRequest
                );

        return contactRequestMapper.toResponse(
                savedRequest
        );
    }

    // ==========================================
    // ADMIN : TOUTES LES DEMANDES
    // ==========================================

    public List<ContactRequestResponse> findAll() {

        return contactRequestRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(
                        contactRequestMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : DEMANDE PAR ID
    // ==========================================

    public ContactRequestResponse findById(
            Long id
    ) {

        ContactRequest contactRequest =
                getRequestOrThrow(id);

        return contactRequestMapper.toResponse(
                contactRequest
        );
    }

    // ==========================================
    // ADMIN : FILTRE PAR STATUT
    // ==========================================

    public List<ContactRequestResponse>
    findByStatus(
            RequestStatus status
    ) {

        return contactRequestRepository
                .findByStatusOrderByCreatedAtDesc(
                        status
                )
                .stream()
                .map(
                        contactRequestMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : FILTRE PAR TYPE
    // ==========================================

    public List<ContactRequestResponse>
    findByType(
            RequestType type
    ) {

        return contactRequestRepository
                .findByTypeOrderByCreatedAtDesc(
                        type
                )
                .stream()
                .map(
                        contactRequestMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : FILTRE TYPE + STATUT
    // ==========================================

    public List<ContactRequestResponse>
    findByTypeAndStatus(
            RequestType type,
            RequestStatus status
    ) {

        return contactRequestRepository
                .findByTypeAndStatusOrderByCreatedAtDesc(
                        type,
                        status
                )
                .stream()
                .map(
                        contactRequestMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : FILTRE DYNAMIQUE
    // ==========================================

    public List<ContactRequestResponse> search(
            RequestType type,
            RequestStatus status
    ) {

        if (
                type != null &&
                        status != null
        ) {
            return findByTypeAndStatus(
                    type,
                    status
            );
        }

        if (type != null) {
            return findByType(type);
        }

        if (status != null) {
            return findByStatus(status);
        }

        return findAll();
    }

    // ==========================================
    // ADMIN : MODIFIER STATUT + NOTE
    // ==========================================

    @Transactional
    public ContactRequestResponse updateStatus(
            Long id,
            ContactRequestAdminUpdateRequest request
    ) {

        ContactRequest contactRequest =
                getRequestOrThrow(id);

        contactRequest.setStatus(
                request.getStatus()
        );

        contactRequest.setAdminNote(
                normalize(
                        request.getAdminNote()
                )
        );

        ContactRequest updatedRequest =
                contactRequestRepository.save(
                        contactRequest
                );

        return contactRequestMapper.toResponse(
                updatedRequest
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @Transactional
    public void delete(
            Long id
    ) {

        ContactRequest contactRequest =
                getRequestOrThrow(id);

        contactRequestRepository.delete(
                contactRequest
        );
    }

    // ==========================================
    // RECHERCHE INTERNE
    // ==========================================

    private ContactRequest getRequestOrThrow(
            Long id
    ) {

        return contactRequestRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Demande introuvable."
                                )
                );
    }

    // ==========================================
    // NETTOYAGE NOTE ADMIN
    // ==========================================

    private String normalize(
            String value
    ) {

        if (
                value == null ||
                        value.isBlank()
        ) {
            return null;
        }

        return value.trim();
    }
}