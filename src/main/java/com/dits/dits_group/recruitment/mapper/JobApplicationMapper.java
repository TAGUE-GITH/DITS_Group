package com.dits.dits_group.recruitment.mapper;

import com.dits.dits_group.recruitment.dto.JobApplicationCreateRequest;
import com.dits.dits_group.recruitment.dto.JobApplicationResponse;
import com.dits.dits_group.recruitment.entity.ApplicationStatus;
import com.dits.dits_group.recruitment.entity.JobApplication;
import com.dits.dits_group.recruitment.entity.JobOffer;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {

    // ==========================================
    // REQUEST -> ENTITY
    // ==========================================

    public JobApplication toEntity(
            JobApplicationCreateRequest request,
            JobOffer jobOffer
    ) {
        if (request == null || jobOffer == null) {
            return null;
        }

        return JobApplication.builder()
                .firstName(normalizeRequired(request.getFirstName()))
                .lastName(normalizeRequired(request.getLastName()))
                .email(normalizeEmail(request.getEmail()))
                .phone(normalizeOptional(request.getPhone()))
                .message(normalizeOptional(request.getMessage()))
                .status(ApplicationStatus.PENDING)
                .jobOffer(jobOffer)
                .build();
    }

    // ==========================================
    // ENTITY -> RESPONSE
    // ==========================================

    public JobApplicationResponse toResponse(
            JobApplication application
    ) {
        if (application == null) {
            return null;
        }

        JobOffer jobOffer =
                application.getJobOffer();

        return JobApplicationResponse.builder()
                .id(application.getId())
                .firstName(application.getFirstName())
                .lastName(application.getLastName())
                .email(application.getEmail())
                .phone(application.getPhone())
                .message(application.getMessage())
                .cvOriginalFileName(
                        application.getCvOriginalFileName()
                )
                .cvUrl(
                        application.getCvUrl()
                )
                .cvContentType(
                        application.getCvContentType()
                )
                .cvFileSize(
                        application.getCvFileSize()
                )
                .status(
                        application.getStatus()
                )
                .adminNote(
                        application.getAdminNote()
                )
                .createdAt(
                        application.getCreatedAt()
                )
                .updatedAt(
                        application.getUpdatedAt()
                )
                .jobOfferId(
                        jobOffer != null
                                ? jobOffer.getId()
                                : null
                )
                .jobOfferTitle(
                        jobOffer != null
                                ? jobOffer.getTitle()
                                : null
                )
                .build();
    }

    // ==========================================
    // NORMALISATION
    // ==========================================

    private String normalizeRequired(
            String value
    ) {
        if (value == null) {
            return null;
        }

        return value.trim();
    }

    private String normalizeEmail(
            String value
    ) {
        if (value == null) {
            return null;
        }

        return value
                .trim()
                .toLowerCase();
    }

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