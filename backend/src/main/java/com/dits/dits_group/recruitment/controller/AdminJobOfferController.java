package com.dits.dits_group.recruitment.controller;

import com.dits.dits_group.recruitment.dto.JobOfferRequest;
import com.dits.dits_group.recruitment.dto.JobOfferResponse;
import com.dits.dits_group.recruitment.entity.ContractType;
import com.dits.dits_group.recruitment.service.JobOfferService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/job-offers")
public class AdminJobOfferController {

    private final JobOfferService jobOfferService;

    public AdminJobOfferController(
            JobOfferService jobOfferService
    ) {
        this.jobOfferService = jobOfferService;
    }

    // ==========================================
    // ADMIN : LISTE + FILTRES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<JobOfferResponse>>
    getJobOffers(
            @RequestParam(required = false)
            ContractType contractType,

            @RequestParam(required = false)
            Boolean published
    ) {

        return ResponseEntity.ok(
                jobOfferService.search(
                        contractType,
                        published
                )
        );
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponse>
    getJobOfferById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                jobOfferService.findById(id)
        );
    }

    // ==========================================
    // ADMIN : CRÉER
    // ==========================================

    @PostMapping
    public ResponseEntity<JobOfferResponse>
    createJobOffer(
            @Valid
            @RequestBody
            JobOfferRequest request
    ) {

        JobOfferResponse createdOffer =
                jobOfferService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOffer);
    }

    // ==========================================
    // ADMIN : MODIFIER
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<JobOfferResponse>
    updateJobOffer(
            @PathVariable Long id,

            @Valid
            @RequestBody
            JobOfferRequest request
    ) {

        return ResponseEntity.ok(
                jobOfferService.update(
                        id,
                        request
                )
        );
    }

    // ==========================================
    // ADMIN : PUBLIER / DÉPUBLIER
    // ==========================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobOfferResponse>
    togglePublished(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                jobOfferService
                        .togglePublished(id)
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>>
    deleteJobOffer(
            @PathVariable Long id
    ) {

        jobOfferService.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Offre supprimée avec succès."
                )
        );
    }
}