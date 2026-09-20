package com.dits.dits_group.newsletter.controller;

import com.dits.dits_group.newsletter.dto.NewsletterResponse;
import com.dits.dits_group.newsletter.dto.NewsletterSubscribeRequest;
import com.dits.dits_group.newsletter.service.NewsletterService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/newsletter")
public class NewsletterController {

    private final NewsletterService newsletterService;

    public NewsletterController(
            NewsletterService newsletterService
    ) {
        this.newsletterService = newsletterService;
    }

    // ==========================================
    // PUBLIC : S'ABONNER
    // ==========================================

    @PostMapping("/subscribe")
    public ResponseEntity<NewsletterResponse> subscribe(
            @Valid
            @RequestBody
            NewsletterSubscribeRequest request
    ) {

        NewsletterResponse response =
                newsletterService.subscribe(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==========================================
    // PUBLIC : SE DÉSABONNER
    // ==========================================

    @PostMapping("/unsubscribe")
    public ResponseEntity<NewsletterResponse> unsubscribe(
            @Valid
            @RequestBody
            NewsletterSubscribeRequest request
    ) {

        NewsletterResponse response =
                newsletterService.unsubscribe(
                        request
                );

        return ResponseEntity.ok(
                response
        );
    }
}