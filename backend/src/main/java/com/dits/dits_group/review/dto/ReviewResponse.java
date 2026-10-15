package com.dits.dits_group.review.dto;

import com.dits.dits_group.review.entity.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String clientName,
        int rating,
        String comment,
        ReviewStatus status,
        LocalDateTime createdAt
) {}