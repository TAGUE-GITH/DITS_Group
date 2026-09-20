package com.dits.dits_group.recruitment.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class JobApplicationFileStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );

    private final Path uploadDirectory;

    public JobApplicationFileStorageService(
            @Value("${app.recruitment.cv-upload-dir:uploads/cvs}")
            String uploadDirectory
    ) {
        this.uploadDirectory =
                Paths.get(uploadDirectory)
                        .toAbsolutePath()
                        .normalize();
    }

    // ==========================================
    // INITIALISATION
    // ==========================================

    @PostConstruct
    public void initialize() {
        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Impossible de créer le dossier de stockage des CV.",
                    exception
            );
        }
    }

    // ==========================================
    // STOCKER UN CV
    // ==========================================

    public String store(MultipartFile file) {
        validateFile(file);

        String originalFileName =
                file.getOriginalFilename();

        String extension =
                getExtension(originalFileName);

        String storedFileName =
                UUID.randomUUID()
                        + extension;

        Path targetPath =
                uploadDirectory
                        .resolve(storedFileName)
                        .normalize();

        if (!targetPath.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Chemin de fichier invalide."
            );
        }

        try {
            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return storedFileName;

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Impossible d'enregistrer le CV.",
                    exception
            );
        }
    }

    // ==========================================
    // RÉCUPÉRER LE CHEMIN
    // ==========================================

    public Path getFilePath(String storedFileName) {

        if (
                storedFileName == null
                        || storedFileName.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Nom du fichier invalide."
            );
        }

        Path filePath =
                uploadDirectory
                        .resolve(storedFileName)
                        .normalize();

        if (!filePath.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Chemin du fichier invalide."
            );
        }

        return filePath;
    }

    // ==========================================
    // SUPPRIMER
    // ==========================================

    public void delete(String storedFileName) {

        if (
                storedFileName == null
                        || storedFileName.isBlank()
        ) {
            return;
        }

        Path filePath =
                getFilePath(storedFileName);

        try {
            Files.deleteIfExists(filePath);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Impossible de supprimer le CV.",
                    exception
            );
        }
    }

    // ==========================================
    // VALIDATION
    // ==========================================

    private void validateFile(MultipartFile file) {

        if (
                file == null
                        || file.isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "Le CV est obligatoire."
            );
        }

        String contentType =
                file.getContentType();

        if (
                contentType == null
                        || !ALLOWED_CONTENT_TYPES.contains(contentType)
        ) {
            throw new IllegalArgumentException(
                    "Format de CV non autorisé. Formats acceptés : PDF, DOC et DOCX."
            );
        }

        String originalFileName =
                file.getOriginalFilename();

        if (
                originalFileName == null
                        || originalFileName.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Le nom du CV est invalide."
            );
        }
    }

    // ==========================================
    // EXTENSION
    // ==========================================

    private String getExtension(String fileName) {

        if (
                fileName == null
                        || !fileName.contains(".")
        ) {
            return "";
        }

        return fileName.substring(
                fileName.lastIndexOf(".")
        );
    }
}