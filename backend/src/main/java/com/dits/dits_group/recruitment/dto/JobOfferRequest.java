package com.dits.dits_group.recruitment.dto;

import com.dits.dits_group.recruitment.entity.ContractType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class JobOfferRequest {

    @NotBlank(message = "Le titre est obligatoire.")
    @Size(
            max = 180,
            message = "Le titre ne doit pas dépasser 180 caractères."
    )
    private String title;

    @NotBlank(message = "La description est obligatoire.")
    private String description;

    @NotBlank(message = "La localisation est obligatoire.")
    @Size(
            max = 150,
            message = "La localisation ne doit pas dépasser 150 caractères."
    )
    private String location;

    @NotNull(message = "Le type de contrat est obligatoire.")
    private ContractType contractType;

    private String requirements;

    private LocalDate closingDate;

    private Boolean published;
}