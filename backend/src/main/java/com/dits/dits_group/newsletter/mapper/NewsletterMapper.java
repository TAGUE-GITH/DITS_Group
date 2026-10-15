package com.dits.dits_group.newsletter.mapper;

import com.dits.dits_group.newsletter.dto.NewsletterResponse;
import com.dits.dits_group.newsletter.dto.NewsletterSubscribeRequest;
import com.dits.dits_group.newsletter.entity.NewsletterStatus;
import com.dits.dits_group.newsletter.entity.NewsletterSubscriber;

import org.springframework.stereotype.Component;

@Component
public class NewsletterMapper {

    // ==========================================
    // DTO REQUEST -> ENTITY
    // ==========================================

    public NewsletterSubscriber toEntity(
            NewsletterSubscribeRequest request
    ) {

        if (request == null) {
            return null;
        }

        return NewsletterSubscriber.builder()
                .email(
                        normalizeEmail(
                                request.getEmail()
                        )
                )
                .status(
                        NewsletterStatus.ACTIVE
                )
                .build();
    }

    // ==========================================
    // ENTITY -> DTO RESPONSE
    // ==========================================

    public NewsletterResponse toResponse(
            NewsletterSubscriber subscriber
    ) {

        if (subscriber == null) {
            return null;
        }

        return NewsletterResponse.builder()
                .id(
                        subscriber.getId()
                )
                .email(
                        subscriber.getEmail()
                )
                .status(
                        subscriber.getStatus()
                )
                .subscribedAt(
                        subscriber.getSubscribedAt()
                )
                .updatedAt(
                        subscriber.getUpdatedAt()
                )
                .build();
    }

    // ==========================================
    // NORMALISATION EMAIL
    // ==========================================

    private String normalizeEmail(
            String email
    ) {

        if (
                email == null
                        || email.isBlank()
        ) {
            return null;
        }

        return email
                .trim()
                .toLowerCase();
    }
}