package com.dits.dits_group.common.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String store(MultipartFile file, String folder) {
        try {
            Path targetDir = Paths.get(uploadDir, folder);
            Files.createDirectories(targetDir);

            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + (extension != null ? "." + extension : "");

            Files.copy(
                    file.getInputStream(),
                    targetDir.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/" + folder + "/" + fileName;
        } catch (IOException exception) {
            throw new RuntimeException("Impossible d'enregistrer le fichier.", exception);
        }
    }

    public void delete(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith("/uploads/")) return;

        try {
            Files.deleteIfExists(Paths.get(uploadDir, fileUrl.substring("/uploads/".length())));
        } catch (IOException ignored) {
        }
    }
}