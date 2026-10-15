package com.dits.dits_group.common.storage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Copie en base de donnees d'un fichier envoye (image ou document). L'hebergeur gratuit efface le disque a chaque
 * redeploiement : le disque ne sert que de cache, il est reconstitue a partir de cette table.
 */
@Entity
@Table(name = "stored_file")
@Getter
@NoArgsConstructor
public class StoredFile {

    /** Chemin relatif au dossier des fichiers, par exemple « services/1b2c.jpg » ou « documents/9f8e.pdf ». */
    @Id
    @Column(length = 255)
    private String path;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] data;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public StoredFile(String path, String contentType, byte[] data) {
        this.path = path;
        this.contentType = contentType;
        this.data = data;
    }
}
