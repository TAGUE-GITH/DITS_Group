package com.dits.dits_group.recruitment.mapper;

import com.dits.dits_group.recruitment.dto.JobOfferRequest;
import com.dits.dits_group.recruitment.dto.JobOfferResponse;
import com.dits.dits_group.recruitment.entity.JobOffer;
import org.springframework.stereotype.Component;

@Component
public class JobOfferMapper {

    // ==========================================
    // REQUEST -> ENTITY
    // ==========================================

    public JobOffer toEntity(JobOfferRequest request) {
        if (request == null) {
            return null;
        }

        return JobOffer.builder()
                .title(normalizeRequired(request.getTitle()))
                .description(normalizeRequired(request.getDescription()))
                .location(normalizeRequired(request.getLocation()))
                .contractType(request.getContractType())
                .requirements(normalizeOptional(request.getRequirements()))
                .closingDate(request.getClosingDate())
                .published(
                        request.getPublished() != null
                                && request.getPublished()
                )
                .build();
    }

    // ==========================================
    // ENTITY -> RESPONSE
    // ==========================================

    public JobOfferResponse toResponse(JobOffer jobOffer) {
        if (jobOffer == null) {
            return null;
        }

        return JobOfferResponse.builder()
                .id(jobOffer.getId())
                .title(jobOffer.getTitle())
                .description(jobOffer.getDescription())
                .location(jobOffer.getLocation())
                .contractType(jobOffer.getContractType())
                .requirements(jobOffer.getRequirements())
                .closingDate(jobOffer.getClosingDate())
                .published(jobOffer.isPublished())
                .createdAt(jobOffer.getCreatedAt())
                .updatedAt(jobOffer.getUpdatedAt())
                .build();
    }

    // ==========================================
    // MISE À JOUR D'UNE ENTITY EXISTANTE
    // ==========================================

    public void updateEntity(
            JobOffer jobOffer,
            JobOfferRequest request
    ) {
        if (jobOffer == null || request == null) {
            return;
        }

        jobOffer.setTitle(
                normalizeRequired(request.getTitle())
        );

        jobOffer.setDescription(
                normalizeRequired(request.getDescription())
        );

        jobOffer.setLocation(
                normalizeRequired(request.getLocation())
        );

        jobOffer.setContractType(
                request.getContractType()
        );

        jobOffer.setRequirements(
                normalizeOptional(request.getRequirements())
        );

        jobOffer.setClosingDate(
                request.getClosingDate()
        );

        if (request.getPublished() != null) {
            jobOffer.setPublished(
                    request.getPublished()
            );
        }
    }

    // ==========================================
    // NORMALISATION
    // ==========================================

    private String normalizeRequired(String value) {
        if (value == null) {
            return null;
        }

        return value.trim();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}