package com.dits.dits_group.review.controller;

import com.dits.dits_group.review.dto.ReviewResponse;
import com.dits.dits_group.review.dto.ReviewStatusRequest;
import com.dits.dits_group.review.entity.ReviewStatus;
import com.dits.dits_group.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAll(
            @RequestParam(required = false) ReviewStatus status
    ) {
        return ResponseEntity.ok(reviewService.getAllForAdmin(status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReviewResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ReviewStatusRequest request
    ) {
        return ResponseEntity.ok(reviewService.updateStatus(id, request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}