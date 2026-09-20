package com.dits.dits_group.partner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerRequest {

    @NotBlank(message = "Le nom du partenaire est obligatoire.")
    @Size(
            max = 180,
            message = "Le nom du partenaire ne doit pas dépasser 180 caractères."
    )
    private String name;

    private String description;

    @Size(
            max = 500,
            message = "L'URL du logo ne doit pas dépasser 500 caractères."
    )
    private String logoUrl;

    @Size(
            max = 500,
            message = "L'URL du site web ne doit pas dépasser 500 caractères."
    )
    private String websiteUrl;

    private Boolean published;
}