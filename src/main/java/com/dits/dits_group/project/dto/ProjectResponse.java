package com.dits.dits_group.project.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ProjectResponse {

    private Long id;

    private String title;

    private String description;

    private String clientName;

    private String imageUrl;

    private String technologies;

    private LocalDate completionDate;

    private boolean published;
}