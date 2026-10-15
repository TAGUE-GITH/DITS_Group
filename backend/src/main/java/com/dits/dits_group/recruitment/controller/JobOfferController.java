package com.dits.dits_group.recruitment.controller;

import com.dits.dits_group.recruitment.dto.JobOfferResponse;
import com.dits.dits_group.recruitment.service.JobOfferService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    public JobOfferController(
            JobOfferService jobOfferService
    ) {
        this.jobOfferService = jobOfferService;
    }

    // ==========================================
    // PUBLIC : LISTE DES OFFRES PUBLIÉES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<JobOfferResponse>>
    getPublishedOffers() {

        return ResponseEntity.ok(
                jobOfferService.findPublishedOffers()
        );
    }

    // ==========================================
    // PUBLIC : DÉTAIL D'UNE OFFRE PUBLIÉE
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponse>
    getPublishedOfferById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                jobOfferService
                        .findPublishedOfferById(id)
        );
    }
}