package com.dits.dits_group.document.controller;

import com.dits.dits_group.document.dto.DocumentResponse;
import com.dits.dits_group.document.dto.DocumentUpdateRequest;
import com.dits.dits_group.document.dto.DocumentUploadRequest;
import com.dits.dits_group.document.service.DocumentService;

import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/documents")
public class AdminDocumentController {

    private final DocumentService documentService;

    public AdminDocumentController(
            DocumentService documentService
    ) {
        this.documentService = documentService;
    }

    // ==========================================
    // ADMIN : TOUS LES DOCUMENTS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<DocumentResponse>>
    getAllDocuments() {

        return ResponseEntity.ok(
                documentService.findAll()
        );
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse>
    getDocumentById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                documentService.findById(id)
        );
    }

    // ==========================================
    // ADMIN : UPLOAD
    // multipart/form-data
    // ==========================================

    @PostMapping(
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentResponse>
    uploadDocument(
            @Valid
            @ModelAttribute
            DocumentUploadRequest request
    ) {

        DocumentResponse document =
                documentService.upload(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(document);
    }

    // ==========================================
    // ADMIN : MODIFIER LES INFORMATIONS
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse>
    updateDocument(
            @PathVariable Long id,

            @Valid
            @RequestBody
            DocumentUpdateRequest request
    ) {

        return ResponseEntity.ok(
                documentService.update(
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
    deleteDocument(
            @PathVariable Long id
    ) {

        documentService.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Document supprimé avec succès."
                )
        );
    }

    // ==========================================
    // ADMIN : TÉLÉCHARGER
    // ==========================================

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource>
    downloadDocument(
            @PathVariable Long id
    ) {

        Path filePath =
                documentService
                        .getFilePathForAdmin(
                                id
                        );

        String originalFileName =
                documentService
                        .getOriginalFileNameForAdmin(
                                id
                        );

        String contentType =
                documentService
                        .getContentTypeForAdmin(
                                id
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