package com.dits.dits_group.dashboard.service;

import com.dits.dits_group.article.repository.ArticleRepository;
import com.dits.dits_group.dashboard.dto.DashboardStatsResponse;
import com.dits.dits_group.document.repository.DocumentRepository;
import com.dits.dits_group.newsletter.entity.NewsletterStatus;
import com.dits.dits_group.newsletter.repository.NewsletterRepository;
import com.dits.dits_group.partner.repository.PartnerRepository;
import com.dits.dits_group.project.repository.ProjectRepository;
import com.dits.dits_group.recruitment.entity.ApplicationStatus;
import com.dits.dits_group.recruitment.repository.JobApplicationRepository;
import com.dits.dits_group.recruitment.repository.JobOfferRepository;
import com.dits.dits_group.request.entity.RequestStatus;
import com.dits.dits_group.request.repository.ContactRequestRepository;
import com.dits.dits_group.service.repository.ServiceRepository;
import com.dits.dits_group.user.entity.UserStatus;
import com.dits.dits_group.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;

    private final ServiceRepository serviceRepository;

    private final ProjectRepository projectRepository;

    private final ArticleRepository articleRepository;

    private final PartnerRepository partnerRepository;

    private final DocumentRepository documentRepository;

    private final ContactRequestRepository contactRequestRepository;

    private final JobOfferRepository jobOfferRepository;

    private final JobApplicationRepository jobApplicationRepository;

    private final NewsletterRepository newsletterRepository;

    public DashboardService(
            UserRepository userRepository,
            ServiceRepository serviceRepository,
            ProjectRepository projectRepository,
            ArticleRepository articleRepository,
            PartnerRepository partnerRepository,
            DocumentRepository documentRepository,
            ContactRequestRepository contactRequestRepository,
            JobOfferRepository jobOfferRepository,
            JobApplicationRepository jobApplicationRepository,
            NewsletterRepository newsletterRepository
    ) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.projectRepository = projectRepository;
        this.articleRepository = articleRepository;
        this.partnerRepository = partnerRepository;
        this.documentRepository = documentRepository;
        this.contactRequestRepository = contactRequestRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.newsletterRepository = newsletterRepository;
    }

    // ==========================================
    // STATISTIQUES GLOBALES DU DASHBOARD
    // ==========================================

    public DashboardStatsResponse getStatistics() {

        return DashboardStatsResponse.builder()

                // ==================================
                // UTILISATEURS
                // ==================================

                .totalUsers(
                        userRepository.count()
                )

                .pendingUsers(
                        userRepository.countByStatus(
                                UserStatus.PENDING
                        )
                )

                .activeUsers(
                        userRepository.countByStatus(
                                UserStatus.ACTIVE
                        )
                )

                .disabledUsers(
                        userRepository.countByStatus(
                                UserStatus.DISABLED
                        )
                )

                // ==================================
                // CONTENUS
                // ==================================

                .totalServices(
                        serviceRepository.count()
                )

                .totalProjects(
                        projectRepository.count()
                )

                .totalArticles(
                        articleRepository.count()
                )

                // ==================================
                // PARTENAIRES
                // ==================================

                .totalPartners(
                        partnerRepository.count()
                )

                .publishedPartners(
                        partnerRepository.countByPublished(
                                true
                        )
                )

                .unpublishedPartners(
                        partnerRepository.countByPublished(
                                false
                        )
                )

                // ==================================
                // DOCUMENTS
                // ==================================

                .totalDocuments(
                        documentRepository.count()
                )

                // ==================================
                // DEMANDES
                // ==================================

                .totalRequests(
                        contactRequestRepository.count()
                )

                .pendingRequests(
                        contactRequestRepository.countByStatus(
                                RequestStatus.PENDING
                        )
                )

                .inProgressRequests(
                        contactRequestRepository.countByStatus(
                                RequestStatus.IN_PROGRESS
                        )
                )

                .processedRequests(
                        contactRequestRepository.countByStatus(
                                RequestStatus.PROCESSED
                        )
                )

                .rejectedRequests(
                        contactRequestRepository.countByStatus(
                                RequestStatus.REJECTED
                        )
                )

                // ==================================
                // OFFRES D'EMPLOI
                // ==================================

                .totalJobOffers(
                        jobOfferRepository.count()
                )

                .publishedJobOffers(
                        jobOfferRepository.countByPublished(
                                true
                        )
                )

                .unpublishedJobOffers(
                        jobOfferRepository.countByPublished(
                                false
                        )
                )

                // ==================================
                // CANDIDATURES
                // ==================================

                .totalApplications(
                        jobApplicationRepository.count()
                )

                .pendingApplications(
                        jobApplicationRepository.countByStatus(
                                ApplicationStatus.PENDING
                        )
                )

                .inReviewApplications(
                        jobApplicationRepository.countByStatus(
                                ApplicationStatus.IN_REVIEW
                        )
                )

                .acceptedApplications(
                        jobApplicationRepository.countByStatus(
                                ApplicationStatus.ACCEPTED
                        )
                )

                .rejectedApplications(
                        jobApplicationRepository.countByStatus(
                                ApplicationStatus.REJECTED
                        )
                )

                // ==================================
                // NEWSLETTER
                // ==================================

                .totalNewsletterSubscribers(
                        newsletterRepository.count()
                )

                .activeNewsletterSubscribers(
                        newsletterRepository.countByStatus(
                                NewsletterStatus.ACTIVE
                        )
                )

                .unsubscribedNewsletterSubscribers(
                        newsletterRepository.countByStatus(
                                NewsletterStatus.UNSUBSCRIBED
                        )
                )

                .build();
    }
}