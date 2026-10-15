package com.dits.dits_group.newsletter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsletterSubscribeRequest {

    @NotBlank(message = "L'adresse email est obligatoire.")
    @Email(message = "L'adresse email n'est pas valide.")
    @Size(
            max = 180,
            message = "L'adresse email ne doit pas dépasser 180 caractères."
    )
    private String email;
}