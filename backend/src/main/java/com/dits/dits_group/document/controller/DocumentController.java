package com.dits.dits_group.document.controller;

import com.dits.dits_group.document.dto.DocumentResponse;
import com.dits.dits_group.document.service.DocumentService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(
            DocumentService documentService
    ) {
        this.documentService = documentService;
    }

    // ==========================================
    // CLIENT : MES DOCUMENTS
    // ==========================================

    @GetMapping("/me")
    public ResponseEntity<List<DocumentResponse>>
    getMyDocuments(
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                documentService.findMyDocuments(
                        email
                )
        );
    }

    // ==========================================
    // CLIENT : DÉTAIL D'UN DE MES DOCUMENTS
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse>
    getMyDocumentById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return ResponseEntity.ok(
                documentService.findMyDocumentById(
                        id,
                        email
                )
        );
    }

    // ==========================================
    // CLIENT : TÉLÉCHARGER MON DOCUMENT
    // ==========================================

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource>
    downloadMyDocument(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        Path filePath =
                documentService
                        .getFilePathForUser(
                                id,
                                email
                        );

        String originalFileName =
                documentService
                        .getOriginalFileNameForUser(
                                id,
                                email
                        );

        String contentType =
                documentService
                        .getContentTypeForUser(
                                id,
                                email
                        );

        Resource resource =
                createResource(
                        filePath
                );

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

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposition.toString()
                )
                .body(resource);
    }

    // ==========================================
    // RESOURCE
    // ==========================================

    private Resource createResource(
            Path filePath
    ) {

        try {

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (!resource.exists()) {
                throw new RuntimeException(
                        "Le fichier demandé est introuvable."
                );
            }

            return resource;

        } catch (MalformedURLException exception) {

            throw new RuntimeException(
                    "Impossible de lire le fichier.",
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
                        ||
                        contentType.isBlank()
        ) {
            return MediaType
                    .APPLICATION_OCTET_STREAM;
        }

        try {

            return MediaType.parseMediaType(
                    contentType
            );

        } catch (Exception exception) {

            return MediaType
                    .APPLICATION_OCTET_STREAM;
        }
    }
}