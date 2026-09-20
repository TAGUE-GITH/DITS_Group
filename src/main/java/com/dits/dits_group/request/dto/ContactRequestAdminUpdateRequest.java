package com.dits.dits_group.request.dto;

import com.dits.dits_group.request.entity.RequestStatus;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactRequestAdminUpdateRequest {

    @NotNull(
            message = "Le statut est obligatoire."
    )
    private RequestStatus status;

    private String adminNote;
}