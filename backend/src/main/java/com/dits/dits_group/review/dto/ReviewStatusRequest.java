package com.dits.dits_group.review.dto;

import com.dits.dits_group.review.entity.ReviewStatus;
import jakarta.validation.constraints.NotNull;

public record ReviewStatusRequest(@NotNull ReviewStatus status) {}