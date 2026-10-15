package com.dits.dits_group.recruitment.controller;

import com.dits.dits_group.recruitment.dto.JobApplicationAdminUpdateRequest;
import com.dits.dits_group.recruitment.dto.JobApplicationResponse;
import com.dits.dits_group.recruitment.entity.ApplicationStatus;
import com.dits.dits_group.recruitment.service.JobApplicationService;

import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/applications")
public class AdminJobApplicationController {

    private final JobApplicationService jobApplicationService;

    public AdminJobApplicationController(
            JobApplicationService jobApplicationService
    ) {
        this.jobApplicationService =
                jobApplicationService;
    }

    // ==========================================
    // ADMIN : LISTE + FILTRES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>>
    getApplications(
            @RequestParam(required = false)
            Long jobOfferId,

            @RequestParam(required = false)
            ApplicationStatus status
    ) {

        return ResponseEntity.ok(
                jobApplicationService.search(
                        jobOfferId,
                        status
                )
        );
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse>
    getApplicationById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                jobApplicationService.findById(id)
        );
    }

    // ==========================================
    // ADMIN : MODIFIER STATUT + NOTE
    // ==========================================

    @PatchMapping("/{id}")
    public ResponseEntity<JobApplicationResponse>
    updateApplication(
            @PathVariable Long id,

            @Valid
            @RequestBody
            JobApplicationAdminUpdateRequest request
    ) {

        return ResponseEntity.ok(
                jobApplicationService.update(
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
    deleteApplication(
            @PathVariable Long id
    ) {

        jobApplicationService.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Candidature supprimée avec succès."
                )
        );
    }

    // ==========================================
    // ADMIN : TÉLÉCHARGER LE CV
    // ==========================================

    @GetMapping("/{id}/cv")
    public ResponseEntity<Resource>
    downloadCv(
            @PathVariable Long id
    ) {

        Path filePath =
                jobApplicationService
                        .getCvPath(id);

        String originalFileName =
                jobApplicationService
                        .getCvOriginalFileName(id);

        String contentType =
                jobApplicationService
                        .getCvContentType(id);

        try {

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (
                    !resource.exists()
                            || !resource.isReadable()
            ) {
                throw new IllegalStateException(
                        "Le CV est introuvable."
                );
            }

            MediaType mediaType =
                    resolveMediaType(
                            contentType
                    );

            ContentDisposition disposition =
                    ContentDisposition
                            .attachment()
                            .filename(
                                    originalFileName,
                                    StandardCharsets.UTF_8
                            )
                            .build();

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            disposition.toString()
                    )
                    .body(resource);

        } catch (MalformedURLException exception) {

            throw new IllegalStateException(
                    "Impossible de lire le CV.",
                    exception
            );
        }
    }

    // ==========================================
    // CONTENT TYPE
    // ==========================================

    private MediaType resolveMediaType(
            String contentType
    ) {

        if (
                contentType == null
                        || contentType.isBlank()
        ) {
            return MediaType
                    .APPLICATION_OCTET_STREAM;
        }

        try {
            return MediaType
                    .parseMediaType(
                            contentType
                    );

        } catch (Exception exception) {

            return MediaType
                    .APPLICATION_OCTET_STREAM;
        }
    }
}