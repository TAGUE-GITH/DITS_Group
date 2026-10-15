package com.dits.dits_group.document.mapper;

import com.dits.dits_group.document.dto.DocumentResponse;
import com.dits.dits_group.document.entity.Document;

import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    // ==========================================
    // ENTITY → RESPONSE DTO
    // ==========================================

    public DocumentResponse toResponse(
            Document document
    ) {

        if (document == null) {
            return null;
        }

        return DocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .description(
                        document.getDescription()
                )
                .originalFileName(
                        document.getOriginalFileName()
                )
                .fileUrl(
                        document.getFileUrl()
                )
                .contentType(
                        document.getContentType()
                )
                .fileSize(
                        document.getFileSize()
                )
                .uploadedAt(
                        document.getUploadedAt()
                )
                .userId(
                        document.getUser() != null
                                ? document
                                .getUser()
                                .getId()
                                : null
                )
                .userEmail(
                        document.getUser() != null
                                ? document
                                .getUser()
                                .getEmail()
                                : null
                )
                .userFirstName(
                        document.getUser() != null
                                ? document
                                .getUser()
                                .getFirstName()
                                : null
                )
                .userLastName(
                        document.getUser() != null
                                ? document
                                .getUser()
                                .getLastName()
                                : null
                )
                .build();
    }
}