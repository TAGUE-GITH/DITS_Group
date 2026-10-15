package com.dits.dits_group.recruitment.repository;

import com.dits.dits_group.recruitment.entity.ContractType;
import com.dits.dits_group.recruitment.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobOfferRepository
        extends JpaRepository<JobOffer, Long> {

    // ==========================================
    // PUBLIC
    // ==========================================

    List<JobOffer>
    findByPublishedTrueOrderByCreatedAtDesc();

    Optional<JobOffer>
    findByIdAndPublishedTrue(Long id);

    // ==========================================
    // ADMIN
    // ==========================================

    List<JobOffer>
    findAllByOrderByCreatedAtDesc();

    List<JobOffer>
    findByContractTypeOrderByCreatedAtDesc(
            ContractType contractType
    );

    List<JobOffer>
    findByPublishedOrderByCreatedAtDesc(
            boolean published
    );

    List<JobOffer>
    findByContractTypeAndPublishedOrderByCreatedAtDesc(
            ContractType contractType,
            boolean published
    );

    long countByPublished(boolean published);
}