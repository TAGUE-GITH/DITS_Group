package com.dits.dits_group.document.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.document.dto.DocumentResponse;
import com.dits.dits_group.document.dto.DocumentUpdateRequest;
import com.dits.dits_group.document.dto.DocumentUploadRequest;
import com.dits.dits_group.document.entity.Document;
import com.dits.dits_group.document.mapper.DocumentMapper;
import com.dits.dits_group.document.repository.DocumentRepository;
import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentFileStorageService fileStorageService;
    private final UserRepository userRepository;

    public DocumentService(
            DocumentRepository documentRepository,
            DocumentMapper documentMapper,
            DocumentFileStorageService fileStorageService,
            UserRepository userRepository
    ) {
        this.documentRepository =
                documentRepository;

        this.documentMapper =
                documentMapper;

        this.fileStorageService =
                fileStorageService;

        this.userRepository =
                userRepository;
    }

    // ==========================================
    // ADMIN : TOUS LES DOCUMENTS
    // ==========================================

    public List<DocumentResponse> findAll() {

        return documentRepository
                .findAllByOrderByUploadedAtDesc()
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    // ==========================================
    // ADMIN : DOCUMENT PAR ID
    // ==========================================

    public DocumentResponse findById(Long id) {

        Document document =
                getDocumentOrThrow(id);

        return documentMapper.toResponse(document);
    }

    // ==========================================
    // CLIENT : MES DOCUMENTS
    // ==========================================

    public List<DocumentResponse> findMyDocuments(
            String email
    ) {

        return documentRepository
                .findByUserEmailOrderByUploadedAtDesc(
                        email
                )
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    // ==========================================
    // CLIENT : MON DOCUMENT PAR ID
    // ==========================================

    public DocumentResponse findMyDocumentById(
            Long id,
            String email
    ) {

        Document document =
                getOwnedDocumentOrThrow(
                        id,
                        email
                );

        return documentMapper.toResponse(document);
    }

    // ==========================================
    // ADMIN : UPLOAD
    // ==========================================

    @Transactional
    public DocumentResponse upload(
            DocumentUploadRequest request
    ) {

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Utilisateur destinataire introuvable."
                                )
                );

        MultipartFile file =
                request.getFile();

        String storedFileName = null;

        try {

            storedFileName =
                    fileStorageService.store(
                            file
                    );

            String originalFileName =
                    cleanOriginalFileName(
                            file.getOriginalFilename()
                    );

            Document document =
                    Document.builder()
                            .title(
                                    request
                                            .getTitle()
                                            .trim()
                            )
                            .description(
                                    normalizeDescription(
                                            request.getDescription()
                                    )
                            )
                            .originalFileName(
                                    originalFileName
                            )
                            .storedFileName(
                                    storedFileName
                            )
                            /*
                             * On enregistre d'abord le document
                             * afin d'obtenir son ID.
                             */
                            .fileUrl("")
                            .contentType(
                                    file.getContentType()
                            )
                            .fileSize(
                                    file.getSize()
                            )
                            .user(user)
                            .build();

            Document savedDocument =
                    documentRepository.save(
                            document
                    );

            /*
             * L'URL publique n'est pas un accès libre.
             * Le contrôleur vérifiera l'utilisateur
             * authentifié avant de télécharger.
             */
            savedDocument.setFileUrl(
                    "/api/documents/"
                            + savedDocument.getId()
                            + "/download"
            );

            Document finalDocument =
                    documentRepository.save(
                            savedDocument
                    );

            return documentMapper.toResponse(
                    finalDocument
            );

        } catch (RuntimeException exception) {

            /*
             * Si PostgreSQL échoue après l'enregistrement
             * physique du fichier, on nettoie le fichier.
             */
            if (storedFileName != null) {

                try {
                    fileStorageService.delete(
                            storedFileName
                    );
                } catch (RuntimeException ignored) {
                    // On conserve l'erreur principale.
                }
            }

            throw exception;
        }
    }

    // ==========================================
    // ADMIN : MODIFIER LES MÉTADONNÉES
    // ==========================================

    @Transactional
    public DocumentResponse update(
            Long id,
            DocumentUpdateRequest request
    ) {

        Document document =
                getDocumentOrThrow(id);

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Utilisateur destinataire introuvable."
                                )
                );

        document.setTitle(
                request.getTitle().trim()
        );

        document.setDescription(
                normalizeDescription(
                        request.getDescription()
                )
        );

        document.setUser(user);

        Document updatedDocument =
                documentRepository.save(
                        document
                );

        return documentMapper.toResponse(
                updatedDocument
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @Transactional
    public void delete(Long id) {

        Document document =
                getDocumentOrThrow(id);

        fileStorageService.delete(
                document.getStoredFileName()
        );

        documentRepository.delete(
                document
        );
    }

    // ==========================================
    // ADMIN : CHEMIN DU FICHIER
    // ==========================================

    public Path getFilePathForAdmin(
            Long id
    ) {

        Document document =
                getDocumentOrThrow(id);

        return fileStorageService.getFilePath(
                document.getStoredFileName()
        );
    }

    // ==========================================
    // CLIENT : CHEMIN DE SON FICHIER
    // ==========================================

    public Path getFilePathForUser(
            Long id,
            String email
    ) {

        Document document =
                getOwnedDocumentOrThrow(
                        id,
                        email
                );

        return fileStorageService.getFilePath(
                document.getStoredFileName()
        );
    }

    // ==========================================
    // ADMIN : ENTITY INTERNE POUR DOWNLOAD
    // ==========================================

    public String getOriginalFileNameForAdmin(
            Long id
    ) {

        return getDocumentOrThrow(id)
                .getOriginalFileName();
    }

    public String getContentTypeForAdmin(
            Long id
    ) {

        return getDocumentOrThrow(id)
                .getContentType();
    }

    // ==========================================
    // CLIENT : INFORMATIONS DOWNLOAD
    // ==========================================

    public String getOriginalFileNameForUser(
            Long id,
            String email
    ) {

        return getOwnedDocumentOrThrow(
                id,
                email
        ).getOriginalFileName();
    }

    public String getContentTypeForUser(
            Long id,
            String email
    ) {

        return getOwnedDocumentOrThrow(
                id,
                email
        ).getContentType();
    }

    // ==========================================
    // DOCUMENT PAR ID
    // ==========================================

    private Document getDocumentOrThrow(
            Long id
    ) {

        return documentRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Document introuvable."
                                )
                );
    }

    // ==========================================
    // DOCUMENT APPARTENANT AU CLIENT
    // ==========================================

    private Document getOwnedDocumentOrThrow(
            Long id,
            String email
    ) {

        Document document =
                getDocumentOrThrow(id);

        if (
                document.getUser() == null
                        ||
                        document.getUser().getEmail() == null
                        ||
                        !document
                                .getUser()
                                .getEmail()
                                .equalsIgnoreCase(email)
        ) {

            /*
             * On retourne volontairement "introuvable"
             * plutôt que "interdit" pour ne pas révéler
             * l'existence d'un document d'un autre client.
             */
            throw new ResourceNotFoundException(
                    "Document introuvable."
            );
        }

        return document;
    }

    // ==========================================
    // NOM ORIGINAL PROPRE
    // ==========================================

    private String cleanOriginalFileName(
            String originalFileName
    ) {

        if (
                originalFileName == null
                        ||
                        originalFileName.isBlank()
        ) {
            return "document";
        }

        return Paths
                .get(originalFileName)
                .getFileName()
                .toString();
    }

    // ==========================================
    // DESCRIPTION
    // ==========================================

    private String normalizeDescription(
            String description
    ) {

        if (
                description == null
                        ||
                        description.isBlank()
        ) {
            return null;
        }

        return description.trim();
    }
}