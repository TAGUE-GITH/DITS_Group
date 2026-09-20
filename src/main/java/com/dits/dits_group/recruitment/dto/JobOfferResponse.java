package com.dits.dits_group.recruitment.dto;

import com.dits.dits_group.recruitment.entity.ContractType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class JobOfferResponse {

    private Long id;

    private String title;

    private String description;

    private String location;

    private ContractType contractType;

    private String requirements;

    private LocalDate closingDate;

    private boolean published;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}