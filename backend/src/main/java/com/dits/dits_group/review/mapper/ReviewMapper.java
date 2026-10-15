package com.dits.dits_group.review.mapper;

import com.dits.dits_group.review.dto.ReviewResponse;
import com.dits.dits_group.review.entity.Review;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getClient().getFirstName() + " " + review.getClient().getLastName(),
                review.getRating(),
                review.getComment(),
                review.getStatus(),
                review.getCreatedAt()
        );
    }

    public List<ReviewResponse> toResponseList(List<Review> reviews) {
        return reviews.stream().map(this::toResponse).toList();
    }
}