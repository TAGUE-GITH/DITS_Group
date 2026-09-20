package com.dits.dits_group.request.controller;

import com.dits.dits_group.request.dto.ContactRequestCreateRequest;
import com.dits.dits_group.request.dto.ContactRequestResponse;
import com.dits.dits_group.request.service.ContactRequestService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class ContactRequestController {

    private final ContactRequestService contactRequestService;

    public ContactRequestController(
            ContactRequestService contactRequestService
    ) {
        this.contactRequestService =
                contactRequestService;
    }

    // ==========================================
    // PUBLIC : ENVOYER UNE DEMANDE
    // ==========================================

    @PostMapping
    public ResponseEntity<ContactRequestResponse>
    createRequest(
            @Valid
            @RequestBody
            ContactRequestCreateRequest request
    ) {

        ContactRequestResponse createdRequest =
                contactRequestService.create(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRequest);
    }
}