package com.dits.dits_group.partner.mapper;

import com.dits.dits_group.partner.dto.PartnerRequest;
import com.dits.dits_group.partner.dto.PartnerResponse;
import com.dits.dits_group.partner.entity.Partner;

import org.springframework.stereotype.Component;

@Component
public class PartnerMapper {

    // ==========================================
    // REQUEST -> ENTITY
    // ==========================================

    public Partner toEntity(
            PartnerRequest request
    ) {

        if (request == null) {
            return null;
        }

        return Partner.builder()
                .name(
                        normalizeRequired(
                                request.getName()
                        )
                )
                .description(
                        normalizeOptional(
                                request.getDescription()
                        )
                )
                .logoUrl(
                        normalizeOptional(
                                request.getLogoUrl()
                        )
                )
                .websiteUrl(
                        normalizeOptional(
                                request.getWebsiteUrl()
                        )
                )
                .published(
                        Boolean.TRUE.equals(
                                request.getPublished()
                        )
                )
                .build();
    }

    // ==========================================
    // ENTITY -> RESPONSE
    // ==========================================

    public PartnerResponse toResponse(
            Partner partner
    ) {

        if (partner == null) {
            return null;
        }

        return PartnerResponse.builder()
                .id(
                        partner.getId()
                )
                .name(
                        partner.getName()
                )
                .description(
                        partner.getDescription()
                )
                .logoUrl(
                        partner.getLogoUrl()
                )
                .websiteUrl(
                        partner.getWebsiteUrl()
                )
                .published(
                        partner.isPublished()
                )
                .createdAt(
                        partner.getCreatedAt()
                )
                .updatedAt(
                        partner.getUpdatedAt()
                )
                .build();
    }

    // ==========================================
    // UPDATE ENTITY
    // ==========================================

    public void updateEntity(
            Partner partner,
            PartnerRequest request
    ) {

        if (
                partner == null
                        || request == null
        ) {
            return;
        }

        partner.setName(
                normalizeRequired(
                        request.getName()
                )
        );

        partner.setDescription(
                normalizeOptional(
                        request.getDescription()
                )
        );

        partner.setLogoUrl(
                normalizeOptional(
                        request.getLogoUrl()
                )
        );

        partner.setWebsiteUrl(
                normalizeOptional(
                        request.getWebsiteUrl()
                )
        );

        if (
                request.getPublished() != null
        ) {
            partner.setPublished(
                    request.getPublished()
            );
        }
    }

    // ==========================================
    // NORMALISATION OBLIGATOIRE
    // ==========================================

    private String normalizeRequired(
            String value
    ) {

        if (value == null) {
            return null;
        }

        return value.trim();
    }

    // ==========================================
    // NORMALISATION OPTIONNELLE
    // ==========================================

    private String normalizeOptional(
            String value
    ) {

        if (
                value == null
                        || value.isBlank()
        ) {
            return null;
        }

        return value.trim();
    }
}