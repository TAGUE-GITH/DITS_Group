package com.dits.dits_group.recruitment.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.recruitment.dto.JobApplicationAdminUpdateRequest;
import com.dits.dits_group.recruitment.dto.JobApplicationCreateRequest;
import com.dits.dits_group.recruitment.dto.JobApplicationResponse;
import com.dits.dits_group.recruitment.entity.ApplicationStatus;
import com.dits.dits_group.recruitment.entity.JobApplication;
import com.dits.dits_group.recruitment.entity.JobOffer;
import com.dits.dits_group.recruitment.mapper.JobApplicationMapper;
import com.dits.dits_group.recruitment.repository.JobApplicationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final JobOfferService jobOfferService;
    private final JobApplicationFileStorageService fileStorageService;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            JobApplicationMapper jobApplicationMapper,
            JobOfferService jobOfferService,
            JobApplicationFileStorageService fileStorageService
    ) {
        this.jobApplicationRepository =
                jobApplicationRepository;

        this.jobApplicationMapper =
                jobApplicationMapper;

        this.jobOfferService =
                jobOfferService;

        this.fileStorageService =
                fileStorageService;
    }

    // ==========================================
    // PUBLIC : POSTULER À UNE OFFRE
    // ==========================================

    @Transactional
    public JobApplicationResponse create(
            Long jobOfferId,
            JobApplicationCreateRequest request
    ) {

        JobOffer jobOffer =
                jobOfferService
                        .getJobOfferEntityOrThrow(
                                jobOfferId
                        );

        // L'offre doit être publiée.
        if (!jobOffer.isPublished()) {
            throw new ResourceNotFoundException(
                    "Offre d'emploi introuvable."
            );
        }

        // Vérification de la date limite.
        if (
                jobOffer.getClosingDate() != null
                        && jobOffer
                        .getClosingDate()
                        .isBefore(LocalDate.now())
        ) {
            throw new IllegalArgumentException(
                    "La date limite de candidature est dépassée."
            );
        }

        String storedFileName = null;

        try {

            // ==================================
            // STOCKAGE DU CV
            // ==================================

            storedFileName =
                    fileStorageService.store(
                            request.getCv()
                    );

            // ==================================
            // CRÉATION DE LA CANDIDATURE
            // ==================================

            JobApplication application =
                    jobApplicationMapper.toEntity(
                            request,
                            jobOffer
                    );

            application.setCvOriginalFileName(
                    request.getCv()
                            .getOriginalFilename()
            );

            application.setCvStoredFileName(
                    storedFileName
            );

            application.setCvContentType(
                    request.getCv()
                            .getContentType()
            );

            application.setCvFileSize(
                    request.getCv()
                            .getSize()
            );

            /*
             * L'URL finale dépend de l'id généré
             * par PostgreSQL.
             */
            application.setCvUrl("");

            JobApplication savedApplication =
                    jobApplicationRepository.save(
                            application
                    );

            // ==================================
            // URL DE TÉLÉCHARGEMENT ADMIN
            // ==================================

            savedApplication.setCvUrl(
                    "/api/admin/applications/"
                            + savedApplication.getId()
                            + "/cv"
            );

            JobApplication finalApplication =
                    jobApplicationRepository.save(
                            savedApplication
                    );

            return jobApplicationMapper
                    .toResponse(
                            finalApplication
                    );

        } catch (RuntimeException exception) {

            /*
             * Si l'enregistrement en base échoue,
             * on supprime le fichier déjà stocké.
             */
            if (storedFileName != null) {

                try {
                    fileStorageService.delete(
                            storedFileName
                    );
                } catch (RuntimeException ignored) {
                    // On conserve l'erreur principale.
                }
            }

            throw exception;
        }
    }

    // ==========================================
    // ADMIN : TOUTES LES CANDIDATURES
    // ==========================================

    public List<JobApplicationResponse> findAll() {

        return jobApplicationRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(
                        jobApplicationMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    public JobApplicationResponse findById(
            Long id
    ) {

        return jobApplicationMapper
                .toResponse(
                        getApplicationOrThrow(id)
                );
    }

    // ==========================================
    // ADMIN : PAR STATUT
    // ==========================================

    public List<JobApplicationResponse> findByStatus(
            ApplicationStatus status
    ) {

        return jobApplicationRepository
                .findByStatusOrderByCreatedAtDesc(
                        status
                )
                .stream()
                .map(
                        jobApplicationMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : PAR OFFRE
    // ==========================================

    public List<JobApplicationResponse> findByJobOfferId(
            Long jobOfferId
    ) {

        return jobApplicationRepository
                .findByJobOfferIdOrderByCreatedAtDesc(
                        jobOfferId
                )
                .stream()
                .map(
                        jobApplicationMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : OFFRE + STATUT
    // ==========================================

    public List<JobApplicationResponse>
    findByJobOfferIdAndStatus(
            Long jobOfferId,
            ApplicationStatus status
    ) {

        return jobApplicationRepository
                .findByJobOfferIdAndStatusOrderByCreatedAtDesc(
                        jobOfferId,
                        status
                )
                .stream()
                .map(
                        jobApplicationMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : FILTRES
    // ==========================================

    public List<JobApplicationResponse> search(
            Long jobOfferId,
            ApplicationStatus status
    ) {

        if (
                jobOfferId != null
                        && status != null
        ) {
            return findByJobOfferIdAndStatus(
                    jobOfferId,
                    status
            );
        }

        if (jobOfferId != null) {
            return findByJobOfferId(
                    jobOfferId
            );
        }

        if (status != null) {
            return findByStatus(
                    status
            );
        }

        return findAll();
    }

    // ==========================================
    // ADMIN : MODIFIER STATUT + NOTE
    // ==========================================

    @Transactional
    public JobApplicationResponse update(
            Long id,
            JobApplicationAdminUpdateRequest request
    ) {

        JobApplication application =
                getApplicationOrThrow(id);

        application.setStatus(
                request.getStatus()
        );

        application.setAdminNote(
                normalize(
                        request.getAdminNote()
                )
        );

        JobApplication updatedApplication =
                jobApplicationRepository.save(
                        application
                );

        return jobApplicationMapper
                .toResponse(
                        updatedApplication
                );
    }

    // ==========================================
    // ADMIN : SUPPRIMER UNE CANDIDATURE
    // ==========================================

    @Transactional
    public void delete(
            Long id
    ) {

        JobApplication application =
                getApplicationOrThrow(id);

        String storedFileName =
                application
                        .getCvStoredFileName();

        jobApplicationRepository.delete(
                application
        );

        fileStorageService.delete(
                storedFileName
        );
    }

    // ==========================================
    // ADMIN : CHEMIN DU CV
    // ==========================================

    public Path getCvPath(
            Long id
    ) {

        JobApplication application =
                getApplicationOrThrow(id);

        return fileStorageService
                .getFilePath(
                        application
                                .getCvStoredFileName()
                );
    }

    // ==========================================
    // ADMIN : NOM ORIGINAL DU CV
    // ==========================================

    public String getCvOriginalFileName(
            Long id
    ) {

        return getApplicationOrThrow(id)
                .getCvOriginalFileName();
    }

    // ==========================================
    // ADMIN : CONTENT TYPE DU CV
    // ==========================================

    public String getCvContentType(
            Long id
    ) {

        return getApplicationOrThrow(id)
                .getCvContentType();
    }

    // ==========================================
    // COMPTER LES CANDIDATURES D'UNE OFFRE
    // ==========================================

    public long countByJobOfferId(
            Long jobOfferId
    ) {

        return jobApplicationRepository
                .countByJobOfferId(
                        jobOfferId
                );
    }

    // ==========================================
    // ENTITY INTERNE
    // ==========================================

    public JobApplication getApplicationEntityOrThrow(
            Long id
    ) {

        return getApplicationOrThrow(id);
    }

    // ==========================================
    // MÉTHODE PRIVÉE
    // ==========================================

    private JobApplication getApplicationOrThrow(
            Long id
    ) {

        return jobApplicationRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Candidature introuvable."
                                )
                );
    }

    // ==========================================
    // NORMALISATION
    // ==========================================

    private String normalize(
            String value
    ) {

        if (
                value == null
                        || value.isBlank()
        ) {
            return null;
        }

        return value.trim();
    }
}