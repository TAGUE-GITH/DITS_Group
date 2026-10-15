package com.dits.dits_group.document.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DocumentResponse {

    private Long id;

    private String title;

    private String description;

    private String originalFileName;

    private String fileUrl;

    private String contentType;

    private Long fileSize;

    private LocalDateTime uploadedAt;

    private Long userId;

    private String userEmail;

    private String userFirstName;

    private String userLastName;
}