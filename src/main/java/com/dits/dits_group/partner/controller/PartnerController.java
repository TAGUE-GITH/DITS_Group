package com.dits.dits_group.partner.controller;

import com.dits.dits_group.partner.dto.PartnerResponse;
import com.dits.dits_group.partner.service.PartnerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(
            PartnerService partnerService
    ) {
        this.partnerService = partnerService;
    }

    // ==========================================
    // PUBLIC : LISTE DES PARTENAIRES PUBLIÉS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<PartnerResponse>> getPublishedPartners() {

        return ResponseEntity.ok(
                partnerService.findPublishedPartners()
        );
    }

    // ==========================================
    // PUBLIC : DÉTAIL D'UN PARTENAIRE PUBLIÉ
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponse> getPublishedPartnerById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                partnerService.findPublishedPartnerById(id)
        );
    }
}