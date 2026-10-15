package com.dits.dits_group.review.controller;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.review.dto.ReviewRequest;
import com.dits.dits_group.review.dto.ReviewResponse;
import com.dits.dits_group.review.dto.ReviewSummaryResponse;
import com.dits.dits_group.review.service.ReviewService;
import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    private User currentUser(Jwt jwt) {
        return userRepository.findByEmail(jwt.getSubject())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable."));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getApprovedReviews() {
        return ResponseEntity.ok(reviewService.getApprovedReviews());
    }

    @GetMapping("/summary")
    public ResponseEntity<ReviewSummaryResponse> getSummary() {
        return ResponseEntity.ok(reviewService.getSummary());
    }

    @GetMapping("/me")
    public ResponseEntity<ReviewResponse> getMyReview(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(reviewService.getMyReview(currentUser(jwt)));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ReviewRequest request
    ) {
        return ResponseEntity.ok(reviewService.create(currentUser(jwt), request));
    }
}