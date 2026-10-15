package com.dits.dits_group.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    // ==========================================
    // UTILISATEURS
    // ==========================================

    private long totalUsers;
    private long pendingUsers;
    private long activeUsers;
    private long disabledUsers;

    // ==========================================
    // CONTENUS
    // ==========================================

    private long totalServices;
    private long totalProjects;
    private long totalArticles;

    // ==========================================
    // PARTENAIRES
    // ==========================================

    private long totalPartners;
    private long publishedPartners;
    private long unpublishedPartners;

    // ==========================================
    // DOCUMENTS
    // ==========================================

    private long totalDocuments;

    // ==========================================
    // DEMANDES
    // ==========================================

    private long totalRequests;
    private long pendingRequests;
    private long inProgressRequests;
    private long processedRequests;
    private long rejectedRequests;

    // ==========================================
    // OFFRES D'EMPLOI
    // ==========================================

    private long totalJobOffers;
    private long publishedJobOffers;
    private long unpublishedJobOffers;

    // ==========================================
    // CANDIDATURES
    // ==========================================

    private long totalApplications;
    private long pendingApplications;
    private long inReviewApplications;
    private long acceptedApplications;
    private long rejectedApplications;

    // ==========================================
    // NEWSLETTER
    // ==========================================

    private long totalNewsletterSubscribers;
    private long activeNewsletterSubscribers;
    private long unsubscribedNewsletterSubscribers;
}