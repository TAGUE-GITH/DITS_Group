package com.dits.dits_group.partner.mapper;

import com.dits.dits_group.partner.dto.PartnerRequest;
import com.dits.dits_group.partner.dto.PartnerResponse;
import com.dits.dits_group.partner.entity.Partner;

import org.springframework.stereotype.Component;

@Component
public class PartnerMapper {

    public Partner toEntity(PartnerRequest request) {
        if (request == null) return null;

        return Partner.builder()
                .name(normalizeRequired(request.getName()))
                .description(normalizeOptional(request.getDescription()))
                .websiteUrl(normalizeOptional(request.getWebsiteUrl()))
                .published(Boolean.TRUE.equals(request.getPublished()))
                .build();
    }

    public PartnerResponse toResponse(Partner partner) {
        if (partner == null) return null;

        return PartnerResponse.builder()
                .id(partner.getId())
                .name(partner.getName())
                .description(partner.getDescription())
                .logoUrl(partner.getLogoUrl())
                .websiteUrl(partner.getWebsiteUrl())
                .published(partner.isPublished())
                .createdAt(partner.getCreatedAt())
                .updatedAt(partner.getUpdatedAt())
                .build();
    }

    public void updateEntity(Partner partner, PartnerRequest request) {
        if (partner == null || request == null) return;

        partner.setName(normalizeRequired(request.getName()));
        partner.setDescription(normalizeOptional(request.getDescription()));
        partner.setWebsiteUrl(normalizeOptional(request.getWebsiteUrl()));

        if (request.getPublished() != null) {
            partner.setPublished(request.getPublished());
        }
    }

    private String normalizeRequired(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeOptional(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}