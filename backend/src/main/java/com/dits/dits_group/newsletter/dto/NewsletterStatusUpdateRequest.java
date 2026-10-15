package com.dits.dits_group.newsletter.dto;

import com.dits.dits_group.newsletter.entity.NewsletterStatus;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsletterStatusUpdateRequest {

    @NotNull(message = "Le statut est obligatoire.")
    private NewsletterStatus status;
}