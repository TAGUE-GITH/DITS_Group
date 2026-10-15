package com.dits.dits_group.request.dto;

import com.dits.dits_group.request.entity.RequestType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactRequestCreateRequest {

    @NotBlank(
            message = "Le prénom est obligatoire."
    )
    @Size(
            max = 100,
            message = "Le prénom ne peut pas dépasser 100 caractères."
    )
    private String firstName;

    @NotBlank(
            message = "Le nom est obligatoire."
    )
    @Size(
            max = 100,
            message = "Le nom ne peut pas dépasser 100 caractères."
    )
    private String lastName;

    @NotBlank(
            message = "L'adresse email est obligatoire."
    )
    @Email(
            message = "L'adresse email n'est pas valide."
    )
    @Size(
            max = 180,
            message = "L'adresse email ne peut pas dépasser 180 caractères."
    )
    private String email;

    @Size(
            max = 30,
            message = "Le numéro de téléphone ne peut pas dépasser 30 caractères."
    )
    private String phone;

    @Size(
            max = 180,
            message = "Le nom de l'entreprise ne peut pas dépasser 180 caractères."
    )
    private String company;

    @NotNull(
            message = "Le type de demande est obligatoire."
    )
    private RequestType type;

    @NotBlank(
            message = "Le sujet est obligatoire."
    )
    @Size(
            max = 200,
            message = "Le sujet ne peut pas dépasser 200 caractères."
    )
    private String subject;

    @NotBlank(
            message = "Le message est obligatoire."
    )
    private String message;
}