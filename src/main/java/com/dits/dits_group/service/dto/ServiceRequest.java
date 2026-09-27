package com.dits.dits_group.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ServiceRequest {

    @NotBlank(message = "Le titre est obligatoire.")
    @Size(max = 150, message = "Le titre ne peut pas dépasser 150 caractères.")
    private String title;

    @NotBlank(message = "La description est obligatoire.")
    private String description;

    private Boolean active;

    private MultipartFile image;
}