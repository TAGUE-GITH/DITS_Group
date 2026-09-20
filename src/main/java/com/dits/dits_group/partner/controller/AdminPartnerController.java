package com.dits.dits_group.partner.controller;

import com.dits.dits_group.partner.dto.PartnerRequest;
import com.dits.dits_group.partner.dto.PartnerResponse;
import com.dits.dits_group.partner.service.PartnerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/partners")
public class AdminPartnerController {

    private final PartnerService partnerService;

    public AdminPartnerController(
            PartnerService partnerService
    ) {
        this.partnerService = partnerService;
    }

    // ==========================================
    // ADMIN : LISTE + RECHERCHE + FILTRE
    // ==========================================

    @GetMapping
    public ResponseEntity<List<PartnerResponse>> getPartners(
            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            Boolean published
    ) {

        return ResponseEntity.ok(
                partnerService.search(
                        search,
                        published
                )
        );
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponse> getPartnerById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                partnerService.findById(id)
        );
    }

    // ==========================================
    // ADMIN : CRÉER
    // ==========================================

    @PostMapping
    public ResponseEntity<PartnerResponse> createPartner(
            @Valid
            @RequestBody
            PartnerRequest request
    ) {

        PartnerResponse createdPartner =
                partnerService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPartner);
    }

    // ==========================================
    // ADMIN : MODIFIER
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<PartnerResponse> updatePartner(
            @PathVariable Long id,

            @Valid
            @RequestBody
            PartnerRequest request
    ) {

        return ResponseEntity.ok(
                partnerService.update(
                        id,
                        request
                )
        );
    }

    // ==========================================
    // ADMIN : PUBLIER / MASQUER
    // ==========================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<PartnerResponse> togglePublished(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                partnerService.togglePublished(id)
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePartner(
            @PathVariable Long id
    ) {

        partnerService.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Partenaire supprimé avec succès."
                )
        );
    }

    // ==========================================
    // ADMIN : STATISTIQUES
    // ==========================================

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStatistics() {

        return ResponseEntity.ok(
                Map.of(
                        "total",
                        partnerService.countAll(),

                        "published",
                        partnerService.countPublished(),

                        "unpublished",
                        partnerService.countUnpublished()
                )
        );
    }
}