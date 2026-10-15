package com.dits.dits_group.newsletter.controller;

import com.dits.dits_group.newsletter.dto.NewsletterResponse;
import com.dits.dits_group.newsletter.dto.NewsletterStatusUpdateRequest;
import com.dits.dits_group.newsletter.entity.NewsletterStatus;
import com.dits.dits_group.newsletter.service.NewsletterService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/newsletter")
public class AdminNewsletterController {

    private final NewsletterService newsletterService;

    public AdminNewsletterController(
            NewsletterService newsletterService
    ) {
        this.newsletterService =
                newsletterService;
    }

    // ==========================================
    // ADMIN : LISTE + RECHERCHE + FILTRE
    // ==========================================

    @GetMapping
    public ResponseEntity<List<NewsletterResponse>> getSubscribers(
            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            NewsletterStatus status
    ) {

        return ResponseEntity.ok(
                newsletterService.search(
                        search,
                        status
                )
        );
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<NewsletterResponse> getSubscriberById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                newsletterService.findById(id)
        );
    }

    // ==========================================
    // ADMIN : MODIFIER LE STATUT
    // ==========================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<NewsletterResponse> updateStatus(
            @PathVariable Long id,

            @Valid
            @RequestBody
            NewsletterStatusUpdateRequest request
    ) {

        return ResponseEntity.ok(
                newsletterService.updateStatus(
                        id,
                        request
                )
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubscriber(
            @PathVariable Long id
    ) {

        newsletterService.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Abonnement newsletter supprimé avec succès."
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
                        newsletterService.countAll(),

                        "active",
                        newsletterService.countActive(),

                        "unsubscribed",
                        newsletterService.countUnsubscribed()
                )
        );
    }
}