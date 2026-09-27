package com.dits.dits_group.partner.controller;

import com.dits.dits_group.partner.dto.PartnerRequest;
import com.dits.dits_group.partner.dto.PartnerResponse;
import com.dits.dits_group.partner.service.PartnerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/partners")
public class AdminPartnerController {

    private final PartnerService partnerService;

    public AdminPartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public ResponseEntity<List<PartnerResponse>> getPartners(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean published
    ) {
        return ResponseEntity.ok(partnerService.search(search, published));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponse> getPartnerById(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.findById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PartnerResponse> createPartner(@Valid @ModelAttribute PartnerRequest request) {
        PartnerResponse createdPartner = partnerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPartner);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PartnerResponse> updatePartner(
            @PathVariable Long id,
            @Valid @ModelAttribute PartnerRequest request
    ) {
        return ResponseEntity.ok(partnerService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PartnerResponse> togglePublished(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.togglePublished(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePartner(@PathVariable Long id) {
        partnerService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Partenaire supprimé avec succès."));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        return ResponseEntity.ok(Map.of(
                "total", partnerService.countAll(),
                "published", partnerService.countPublished(),
                "unpublished", partnerService.countUnpublished()
        ));
    }
}