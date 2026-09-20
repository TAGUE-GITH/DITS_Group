package com.dits.dits_group.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProjectRequest {

    @NotBlank(
            message = "Le titre de la réalisation est obligatoire."
    )
    @Size(
            max = 180,
            message = "Le titre ne peut pas dépasser 180 caractères."
    )
    private String title;

    @NotBlank(
            message = "La description est obligatoire."
    )
    private String description;

    @Size(
            max = 150,
            message = "Le nom du client ne peut pas dépasser 150 caractères."
    )
    private String clientName;

    @Size(
            max = 500,
            message = "L'URL de l'image est trop longue."
    )
    private String imageUrl;

    private String technologies;

    private LocalDate completionDate;

    private Boolean published;
}