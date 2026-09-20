package com.dits.dits_group.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class DocumentUploadRequest {

    @NotBlank(
            message = "Le titre du document est obligatoire."
    )
    @Size(
            max = 180,
            message = "Le titre ne peut pas dépasser 180 caractères."
    )
    private String title;

    private String description;

    @NotNull(
            message = "L'utilisateur destinataire est obligatoire."
    )
    private Long userId;

    @NotNull(
            message = "Le fichier est obligatoire."
    )
    private MultipartFile file;
}