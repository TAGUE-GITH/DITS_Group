package com.dits.dits_group.common.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Images du site (services, realisations, articles, partenaires). Chaque fichier est enregistre en base de donnees
 * et copie sur le disque, d'ou il est servi sous /uploads/... ; la copie disque est reconstituee apres un redeploiement.
 */
@Service
public class FileStorageService {

    private final StoredFileRepository storedFiles;
    private final Path root;

    public FileStorageService(StoredFileRepository storedFiles, @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.storedFiles = storedFiles;
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Transactional
    public String store(MultipartFile file, String folder) {
        try {
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String path = folder + "/" + UUID.randomUUID() + (extension != null ? "." + extension.toLowerCase() : "");
            byte[] data = file.getBytes();
            storedFiles.save(new StoredFile(path, file.getContentType(), data));
            write(path, data);
            return "/uploads/" + path;
        } catch (IOException exception) {
            throw new RuntimeException("Impossible d'enregistrer le fichier.", exception);
        }
    }

    /** Fichier deja en memoire (photos du contenu de depart). */
    @Transactional
    public String store(byte[] data, String contentType, String extension, String folder) {
        try {
            String path = folder + "/" + UUID.randomUUID() + "." + extension;
            storedFiles.save(new StoredFile(path, contentType, data));
            write(path, data);
            return "/uploads/" + path;
        } catch (IOException exception) {
            throw new RuntimeException("Impossible d'enregistrer le fichier.", exception);
        }
    }

    @Transactional
    public void delete(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith("/uploads/")) return;
        String path = fileUrl.substring("/uploads/".length());
        storedFiles.deleteById(path);
        try {
            Files.deleteIfExists(resolve(path));
        } catch (IOException ignored) {
            // la copie disque n'est qu'un cache
        }
    }

    /** Copie un fichier sur le disque (dossier racine des fichiers envoyes). */
    void write(String path, byte[] data) throws IOException {
        Path target = resolve(path);
        Files.createDirectories(target.getParent());
        Files.write(target, data);
    }

    Path resolve(String path) {
        Path target = root.resolve(path).normalize();
        if (!target.startsWith(root)) throw new IllegalArgumentException("Chemin de fichier invalide.");
        return target;
    }
}
