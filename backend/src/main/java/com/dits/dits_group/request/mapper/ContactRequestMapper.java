package com.dits.dits_group.request.mapper;

import com.dits.dits_group.request.dto.ContactRequestCreateRequest;
import com.dits.dits_group.request.dto.ContactRequestResponse;
import com.dits.dits_group.request.entity.ContactRequest;
import com.dits.dits_group.request.entity.RequestStatus;

import org.springframework.stereotype.Component;

@Component
public class ContactRequestMapper {

    // ==========================================
    // CREATE DTO -> ENTITY
    // ==========================================

    public ContactRequest toEntity(
            ContactRequestCreateRequest request
    ) {

        if (request == null) {
            return null;
        }

        return ContactRequest.builder()
                .firstName(
                        request
                                .getFirstName()
                                .trim()
                )
                .lastName(
                        request
                                .getLastName()
                                .trim()
                )
                .email(
                        request
                                .getEmail()
                                .trim()
                                .toLowerCase()
                )
                .phone(
                        normalize(
                                request.getPhone()
                        )
                )
                .company(
                        normalize(
                                request.getCompany()
                        )
                )
                .type(
                        request.getType()
                )
                .subject(
                        request
                                .getSubject()
                                .trim()
                )
                .message(
                        request
                                .getMessage()
                                .trim()
                )
                .status(
                        RequestStatus.PENDING
                )
                .build();
    }

    // ==========================================
    // ENTITY -> RESPONSE DTO
    // ==========================================

    public ContactRequestResponse toResponse(
            ContactRequest request
    ) {

        if (request == null) {
            return null;
        }

        return ContactRequestResponse.builder()
                .id(
                        request.getId()
                )
                .firstName(
                        request.getFirstName()
                )
                .lastName(
                        request.getLastName()
                )
                .email(
                        request.getEmail()
                )
                .phone(
                        request.getPhone()
                )
                .company(
                        request.getCompany()
                )
                .type(
                        request.getType()
                )
                .subject(
                        request.getSubject()
                )
                .message(
                        request.getMessage()
                )
                .status(
                        request.getStatus()
                )
                .adminNote(
                        request.getAdminNote()
                )
                .createdAt(
                        request.getCreatedAt()
                )
                .updatedAt(
                        request.getUpdatedAt()
                )
                .build();
    }

    // ==========================================
    // NETTOYAGE DES CHAMPS OPTIONNELS
    // ==========================================

    private String normalize(
            String value
    ) {

        if (
                value == null ||
                        value.isBlank()
        ) {
            return null;
        }

        return value.trim();
    }
}