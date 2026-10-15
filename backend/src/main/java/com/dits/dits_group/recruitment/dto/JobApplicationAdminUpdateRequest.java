package com.dits.dits_group.recruitment.dto;

import com.dits.dits_group.recruitment.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobApplicationAdminUpdateRequest {

    @NotNull(message = "Le statut est obligatoire.")
    private ApplicationStatus status;

    private String adminNote;
}