package com.dits.dits_group.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServiceResponse {

    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private boolean active;
}