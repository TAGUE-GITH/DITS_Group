package com.dits.dits_group.common.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;

/** Au demarrage, recopie sur le disque les fichiers (images, documents) effaces par un redeploiement. */
@Component
public class StoredFileRestorer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StoredFileRestorer.class);

    private final StoredFileRepository storedFiles;
    private final FileStorageService storage;

    public StoredFileRestorer(StoredFileRepository storedFiles, FileStorageService storage) {
        this.storedFiles = storedFiles;
        this.storage = storage;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(ApplicationArguments args) {
        int restored = 0;
        for (String path : storedFiles.findAllPaths()) {
            if (Files.exists(storage.resolve(path))) continue;
            try {
                storage.write(path, storedFiles.findById(path).orElseThrow().getData());
                restored++;
            } catch (IOException | RuntimeException exception) {
                log.warn("Fichier {} non restauré : {}", path, exception.getMessage());
            }
        }
        if (restored > 0) log.info("{} fichier(s) restauré(s) sur le disque", restored);
    }
}
