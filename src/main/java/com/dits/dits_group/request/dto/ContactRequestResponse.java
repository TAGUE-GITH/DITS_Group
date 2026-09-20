package com.dits.dits_group.request.dto;

import com.dits.dits_group.request.entity.RequestStatus;
import com.dits.dits_group.request.entity.RequestType;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContactRequestResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String company;

    private RequestType type;

    private String subject;

    private String message;

    private RequestStatus status;

    private String adminNote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}