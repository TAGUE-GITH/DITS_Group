package com.dits.dits_group.review.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.review.dto.ReviewRequest;
import com.dits.dits_group.review.dto.ReviewResponse;
import com.dits.dits_group.review.dto.ReviewSummaryResponse;
import com.dits.dits_group.review.entity.Review;
import com.dits.dits_group.review.entity.ReviewStatus;
import com.dits.dits_group.review.mapper.ReviewMapper;
import com.dits.dits_group.review.repository.ReviewRepository;
import com.dits.dits_group.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewResponse> getApprovedReviews() {
        return reviewMapper.toResponseList(
                reviewRepository.findByStatusOrderByCreatedAtDesc(ReviewStatus.APPROVED)
        );
    }

    public ReviewSummaryResponse getSummary() {
        List<Review> approved = reviewRepository.findByStatusOrderByCreatedAtDesc(ReviewStatus.APPROVED);
        double average = approved.stream().mapToInt(Review::getRating).average().orElse(0);
        return new ReviewSummaryResponse(Math.round(average * 10) / 10.0, approved.size());
    }

    public ReviewResponse getMyReview(User client) {
        return reviewRepository.findFirstByClientOrderByCreatedAtDesc(client)
                .map(reviewMapper::toResponse)
                .orElse(null);
    }

    public ReviewResponse create(User client, ReviewRequest request) {
        boolean hasActive = reviewRepository.existsByClientAndStatusIn(
                client, List.of(ReviewStatus.PENDING, ReviewStatus.APPROVED));

        if (hasActive) {
            throw new IllegalArgumentException("Vous avez déjà un avis en attente ou publié.");
        }

        Review review = Review.builder()
                .client(client)
                .rating(request.rating())
                .comment(request.comment())
                .build();

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    public List<ReviewResponse> getAllForAdmin(ReviewStatus status) {
        List<Review> reviews = status != null
                ? reviewRepository.findByStatusOrderByCreatedAtDesc(status)
                : reviewRepository.findAllByOrderByCreatedAtDesc();

        return reviewMapper.toResponseList(reviews);
    }

    public ReviewResponse updateStatus(Long id, ReviewStatus status) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable."));

        review.setStatus(status);
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    public void delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avis introuvable.");
        }

        reviewRepository.deleteById(id);
    }
}