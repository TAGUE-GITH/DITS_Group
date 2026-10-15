package com.dits.dits_group.review.repository;

import com.dits.dits_group.review.entity.Review;
import com.dits.dits_group.review.entity.ReviewStatus;
import com.dits.dits_group.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStatusOrderByCreatedAtDesc(ReviewStatus status);
    List<Review> findAllByOrderByCreatedAtDesc();
    Optional<Review> findFirstByClientOrderByCreatedAtDesc(User client);
    boolean existsByClientAndStatusIn(User client, List<ReviewStatus> statuses);
}