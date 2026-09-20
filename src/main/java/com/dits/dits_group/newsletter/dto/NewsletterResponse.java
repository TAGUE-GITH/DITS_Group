package com.dits.dits_group.newsletter.dto;

import com.dits.dits_group.newsletter.entity.NewsletterStatus;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsletterResponse {

    private Long id;

    private String email;

    private NewsletterStatus status;

    private LocalDateTime subscribedAt;

    private LocalDateTime updatedAt;
}