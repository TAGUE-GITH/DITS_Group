package com.dits.dits_group.request.controller;

import com.dits.dits_group.request.dto.ContactRequestAdminUpdateRequest;
import com.dits.dits_group.request.dto.ContactRequestResponse;
import com.dits.dits_group.request.entity.RequestStatus;
import com.dits.dits_group.request.entity.RequestType;
import com.dits.dits_group.request.service.ContactRequestService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/requests")
public class AdminContactRequestController {

    private final ContactRequestService contactRequestService;

    public AdminContactRequestController(
            ContactRequestService contactRequestService
    ) {
        this.contactRequestService =
                contactRequestService;
    }

    // ==========================================
    // ADMIN : LISTE + FILTRES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<ContactRequestResponse>>
    getRequests(
            @RequestParam(required = false)
            RequestType type,

            @RequestParam(required = false)
            RequestStatus status
    ) {

        return ResponseEntity.ok(
                contactRequestService.search(
                        type,
                        status
                )
        );
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<ContactRequestResponse>
    getRequestById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                contactRequestService.findById(
                        id
                )
        );
    }

    // ==========================================
    // ADMIN : STATUT + NOTE
    // ==========================================

    @PatchMapping("/{id}")
    public ResponseEntity<ContactRequestResponse>
    updateRequest(
            @PathVariable Long id,

            @Valid
            @RequestBody
            ContactRequestAdminUpdateRequest request
    ) {

        return ResponseEntity.ok(
                contactRequestService.updateStatus(
                        id,
                        request
                )
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>>
    deleteRequest(
            @PathVariable Long id
    ) {

        contactRequestService.delete(
                id
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Demande supprimée avec succès."
                )
        );
    }
}