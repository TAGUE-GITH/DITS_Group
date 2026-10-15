package com.dits.dits_group.recruitment.dto;

import com.dits.dits_group.recruitment.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JobApplicationResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String message;

    private String cvOriginalFileName;

    private String cvUrl;

    private String cvContentType;

    private Long cvFileSize;

    private ApplicationStatus status;

    private String adminNote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long jobOfferId;

    private String jobOfferTitle;
}