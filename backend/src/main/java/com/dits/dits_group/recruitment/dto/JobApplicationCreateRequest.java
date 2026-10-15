package com.dits.dits_group.recruitment.dto;

import jakarta.validation.constraints.Email;
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
public class JobApplicationCreateRequest {

    @NotBlank(message = "Le prénom est obligatoire.")
    @Size(
            max = 100,
            message = "Le prénom ne doit pas dépasser 100 caractères."
    )
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(
            max = 100,
            message = "Le nom ne doit pas dépasser 100 caractères."
    )
    private String lastName;

    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "L'adresse email est invalide.")
    @Size(
            max = 180,
            message = "L'email ne doit pas dépasser 180 caractères."
    )
    private String email;

    @Size(
            max = 30,
            message = "Le téléphone ne doit pas dépasser 30 caractères."
    )
    private String phone;

    private String message;

    @NotNull(message = "Le CV est obligatoire.")
    private MultipartFile cv;
}