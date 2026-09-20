package com.dits.dits_group.document.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class DocumentFileStorageService {

    private final Path uploadDirectory;

    public DocumentFileStorageService(
            @Value("${app.documents.upload-dir:uploads/documents}")
            String uploadDirectory
    ) {
        this.uploadDirectory = Paths
                .get(uploadDirectory)
                .toAbsolutePath()
                .normalize();
    }

    // ==========================================
    // CRÉATION DU DOSSIER DE STOCKAGE
    // ==========================================

    @PostConstruct
    public void initializeStorage() {
        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Impossible de créer le dossier de stockage des documents.",
                    exception
            );
        }
    }

    // ==========================================
    // ENREGISTRER UN FICHIER
    // ==========================================

    public String store(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Le fichier est obligatoire et ne peut pas être vide."
            );
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom du fichier est invalide."
            );
        }

        String cleanFileName = Paths
                .get(originalFileName)
                .getFileName()
                .toString();

        String extension = getExtension(cleanFileName);

        String storedFileName =
                UUID.randomUUID() + extension;

        Path destination = uploadDirectory
                .resolve(storedFileName)
                .normalize();

        if (!destination.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Chemin de fichier invalide."
            );
        }

        try (
                InputStream inputStream =
                        file.getInputStream()
        ) {

            Files.copy(
                    inputStream,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return storedFileName;

        } catch (IOException exception) {

            throw new RuntimeException(
                    "Impossible d'enregistrer le fichier.",
                    exception
            );
        }
    }

    // ==========================================
    // RÉCUPÉRER LE CHEMIN D'UN FICHIER
    // ==========================================

    public Path getFilePath(String storedFileName) {

        if (storedFileName == null || storedFileName.isBlank()) {
            throw new IllegalArgumentException(
                    "Nom de fichier invalide."
            );
        }

        Path filePath = uploadDirectory
                .resolve(storedFileName)
                .normalize();

        if (!filePath.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Chemin de fichier invalide."
            );
        }

        if (!Files.exists(filePath)) {
            throw new RuntimeException(
                    "Le fichier demandé n'existe plus sur le serveur."
            );
        }

        return filePath;
    }

    // ==========================================
    // SUPPRIMER UN FICHIER
    // ==========================================

    public void delete(String storedFileName) {

        if (storedFileName == null || storedFileName.isBlank()) {
            return;
        }

        Path filePath = uploadDirectory
                .resolve(storedFileName)
                .normalize();

        if (!filePath.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Chemin de fichier invalide."
            );
        }

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Impossible de supprimer le fichier du serveur.",
                    exception
            );
        }
    }

    // ==========================================
    // EXTENSION
    // ==========================================

    private String getExtension(String fileName) {

        int index = fileName.lastIndexOf(".");

        if (index < 0) {
            return "";
        }

        return fileName
                .substring(index)
                .toLowerCase();
    }
}