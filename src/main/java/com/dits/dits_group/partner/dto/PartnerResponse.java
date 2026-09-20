package com.dits.dits_group.partner.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PartnerResponse {

    private Long id;

    private String name;

    private String description;

    private String logoUrl;

    private String websiteUrl;

    private boolean published;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}