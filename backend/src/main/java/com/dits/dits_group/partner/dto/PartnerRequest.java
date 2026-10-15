package com.dits.dits_group.partner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PartnerRequest {

    @NotBlank
    private String name;

    private String description;

    private String websiteUrl;

    private Boolean published;

    private MultipartFile logo;
}