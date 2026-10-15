package com.dits.dits_group.recruitment.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.recruitment.dto.JobOfferRequest;
import com.dits.dits_group.recruitment.dto.JobOfferResponse;
import com.dits.dits_group.recruitment.entity.ContractType;
import com.dits.dits_group.recruitment.entity.JobOffer;
import com.dits.dits_group.recruitment.mapper.JobOfferMapper;
import com.dits.dits_group.recruitment.repository.JobOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMapper jobOfferMapper;

    public JobOfferService(
            JobOfferRepository jobOfferRepository,
            JobOfferMapper jobOfferMapper
    ) {
        this.jobOfferRepository = jobOfferRepository;
        this.jobOfferMapper = jobOfferMapper;
    }

    // ==========================================
    // PUBLIC : OFFRES PUBLIÉES
    // ==========================================

    public List<JobOfferResponse> findPublishedOffers() {
        return jobOfferRepository
                .findByPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(jobOfferMapper::toResponse)
                .toList();
    }

    // ==========================================
    // PUBLIC : DÉTAIL D'UNE OFFRE PUBLIÉE
    // ==========================================

    public JobOfferResponse findPublishedOfferById(Long id) {
        JobOffer jobOffer =
                jobOfferRepository
                        .findByIdAndPublishedTrue(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Offre d'emploi introuvable."
                                )
                        );

        return jobOfferMapper.toResponse(jobOffer);
    }

    // ==========================================
    // ADMIN : TOUTES LES OFFRES
    // ==========================================

    public List<JobOfferResponse> findAll() {
        return jobOfferRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(jobOfferMapper::toResponse)
                .toList();
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    public JobOfferResponse findById(Long id) {
        return jobOfferMapper.toResponse(
                getJobOfferOrThrow(id)
        );
    }

    // ==========================================
    // ADMIN : FILTRE PAR CONTRAT
    // ==========================================

    public List<JobOfferResponse> findByContractType(
            ContractType contractType
    ) {
        return jobOfferRepository
                .findByContractTypeOrderByCreatedAtDesc(
                        contractType
                )
                .stream()
                .map(jobOfferMapper::toResponse)
                .toList();
    }

    // ==========================================
    // ADMIN : FILTRE PAR PUBLICATION
    // ==========================================

    public List<JobOfferResponse> findByPublished(
            boolean published
    ) {
        return jobOfferRepository
                .findByPublishedOrderByCreatedAtDesc(
                        published
                )
                .stream()
                .map(jobOfferMapper::toResponse)
                .toList();
    }

    // ==========================================
    // ADMIN : FILTRE CONTRAT + PUBLICATION
    // ==========================================

    public List<JobOfferResponse>
    findByContractTypeAndPublished(
            ContractType contractType,
            boolean published
    ) {
        return jobOfferRepository
                .findByContractTypeAndPublishedOrderByCreatedAtDesc(
                        contractType,
                        published
                )
                .stream()
                .map(jobOfferMapper::toResponse)
                .toList();
    }

    // ==========================================
    // ADMIN : RECHERCHE / FILTRES
    // ==========================================

    public List<JobOfferResponse> search(
            ContractType contractType,
            Boolean published
    ) {

        if (
                contractType != null
                        && published != null
        ) {
            return findByContractTypeAndPublished(
                    contractType,
                    published
            );
        }

        if (contractType != null) {
            return findByContractType(
                    contractType
            );
        }

        if (published != null) {
            return findByPublished(
                    published
            );
        }

        return findAll();
    }

    // ==========================================
    // ADMIN : CRÉER UNE OFFRE
    // ==========================================

    @Transactional
    public JobOfferResponse create(
            JobOfferRequest request
    ) {
        JobOffer jobOffer =
                jobOfferMapper.toEntity(request);

        JobOffer savedOffer =
                jobOfferRepository.save(jobOffer);

        return jobOfferMapper.toResponse(
                savedOffer
        );
    }

    // ==========================================
    // ADMIN : MODIFIER UNE OFFRE
    // ==========================================

    @Transactional
    public JobOfferResponse update(
            Long id,
            JobOfferRequest request
    ) {
        JobOffer jobOffer =
                getJobOfferOrThrow(id);

        jobOfferMapper.updateEntity(
                jobOffer,
                request
        );

        JobOffer updatedOffer =
                jobOfferRepository.save(
                        jobOffer
                );

        return jobOfferMapper.toResponse(
                updatedOffer
        );
    }

    // ==========================================
    // ADMIN : PUBLIER / DÉPUBLIER
    // ==========================================

    @Transactional
    public JobOfferResponse togglePublished(
            Long id
    ) {
        JobOffer jobOffer =
                getJobOfferOrThrow(id);

        jobOffer.setPublished(
                !jobOffer.isPublished()
        );

        JobOffer updatedOffer =
                jobOfferRepository.save(
                        jobOffer
                );

        return jobOfferMapper.toResponse(
                updatedOffer
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @Transactional
    public void delete(Long id) {
        JobOffer jobOffer =
                getJobOfferOrThrow(id);

        jobOfferRepository.delete(
                jobOffer
        );
    }

    // ==========================================
    // ENTITY INTERNE
    // ==========================================

    public JobOffer getJobOfferEntityOrThrow(
            Long id
    ) {
        return getJobOfferOrThrow(id);
    }

    // ==========================================
    // MÉTHODE PRIVÉE
    // ==========================================

    private JobOffer getJobOfferOrThrow(
            Long id
    ) {
        return jobOfferRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Offre d'emploi introuvable."
                        )
                );
    }
}