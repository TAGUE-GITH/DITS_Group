package com.dits.dits_group.newsletter.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.newsletter.dto.NewsletterResponse;
import com.dits.dits_group.newsletter.dto.NewsletterStatusUpdateRequest;
import com.dits.dits_group.newsletter.dto.NewsletterSubscribeRequest;
import com.dits.dits_group.newsletter.entity.NewsletterStatus;
import com.dits.dits_group.newsletter.entity.NewsletterSubscriber;
import com.dits.dits_group.newsletter.mapper.NewsletterMapper;
import com.dits.dits_group.newsletter.repository.NewsletterRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class NewsletterService {

    private final NewsletterRepository newsletterRepository;
    private final NewsletterMapper newsletterMapper;

    public NewsletterService(
            NewsletterRepository newsletterRepository,
            NewsletterMapper newsletterMapper
    ) {
        this.newsletterRepository = newsletterRepository;
        this.newsletterMapper = newsletterMapper;
    }

    // ==========================================
    // PUBLIC : S'ABONNER
    // ==========================================

    @Transactional
    public NewsletterResponse subscribe(
            NewsletterSubscribeRequest request
    ) {

        String email =
                normalizeEmail(
                        request.getEmail()
                );

        NewsletterSubscriber existingSubscriber =
                newsletterRepository
                        .findByEmailIgnoreCase(email)
                        .orElse(null);

        /*
         * L'adresse existe déjà.
         */
        if (existingSubscriber != null) {

            /*
             * Déjà abonné.
             */
            if (
                    existingSubscriber.getStatus()
                            == NewsletterStatus.ACTIVE
            ) {
                throw new IllegalArgumentException(
                        "Cette adresse email est déjà abonnée à la newsletter."
                );
            }

            /*
             * Ancien abonné désinscrit :
             * on réactive son abonnement.
             */
            existingSubscriber.setStatus(
                    NewsletterStatus.ACTIVE
            );

            NewsletterSubscriber reactivated =
                    newsletterRepository.save(
                            existingSubscriber
                    );

            return newsletterMapper
                    .toResponse(
                            reactivated
                    );
        }

        /*
         * Nouvel abonné.
         */
        NewsletterSubscriber subscriber =
                newsletterMapper.toEntity(
                        request
                );

        NewsletterSubscriber savedSubscriber =
                newsletterRepository.save(
                        subscriber
                );

        return newsletterMapper
                .toResponse(
                        savedSubscriber
                );
    }

    // ==========================================
    // PUBLIC : SE DÉSABONNER
    // ==========================================

    @Transactional
    public NewsletterResponse unsubscribe(
            NewsletterSubscribeRequest request
    ) {

        String email =
                normalizeEmail(
                        request.getEmail()
                );

        NewsletterSubscriber subscriber =
                newsletterRepository
                        .findByEmailIgnoreCase(email)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Aucun abonnement newsletter n'a été trouvé pour cette adresse email."
                                        )
                        );

        if (
                subscriber.getStatus()
                        == NewsletterStatus.UNSUBSCRIBED
        ) {
            return newsletterMapper
                    .toResponse(
                            subscriber
                    );
        }

        subscriber.setStatus(
                NewsletterStatus.UNSUBSCRIBED
        );

        NewsletterSubscriber updatedSubscriber =
                newsletterRepository.save(
                        subscriber
                );

        return newsletterMapper
                .toResponse(
                        updatedSubscriber
                );
    }

    // ==========================================
    // ADMIN : TOUS LES ABONNÉS
    // ==========================================

    public List<NewsletterResponse> findAll() {

        return newsletterRepository
                .findAllByOrderBySubscribedAtDesc()
                .stream()
                .map(
                        newsletterMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    public NewsletterResponse findById(
            Long id
    ) {

        return newsletterMapper
                .toResponse(
                        getSubscriberOrThrow(id)
                );
    }

    // ==========================================
    // ADMIN : FILTRER PAR STATUT
    // ==========================================

    public List<NewsletterResponse> findByStatus(
            NewsletterStatus status
    ) {

        return newsletterRepository
                .findByStatusOrderBySubscribedAtDesc(
                        status
                )
                .stream()
                .map(
                        newsletterMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : RECHERCHE + FILTRE
    // ==========================================

    public List<NewsletterResponse> search(
            String query,
            NewsletterStatus status
    ) {

        List<NewsletterSubscriber> subscribers;

        if (status != null) {

            subscribers =
                    newsletterRepository
                            .findByStatusOrderBySubscribedAtDesc(
                                    status
                            );

        } else {

            subscribers =
                    newsletterRepository
                            .findAllByOrderBySubscribedAtDesc();
        }

        if (
                query == null
                        || query.isBlank()
        ) {

            return subscribers
                    .stream()
                    .map(
                            newsletterMapper::toResponse
                    )
                    .toList();
        }

        String normalizedQuery =
                query
                        .trim()
                        .toLowerCase();

        return subscribers
                .stream()
                .filter(
                        subscriber ->
                                subscriber
                                        .getEmail()
                                        .toLowerCase()
                                        .contains(
                                                normalizedQuery
                                        )
                )
                .map(
                        newsletterMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : MODIFIER LE STATUT
    // ==========================================

    @Transactional
    public NewsletterResponse updateStatus(
            Long id,
            NewsletterStatusUpdateRequest request
    ) {

        NewsletterSubscriber subscriber =
                getSubscriberOrThrow(id);

        subscriber.setStatus(
                request.getStatus()
        );

        NewsletterSubscriber updatedSubscriber =
                newsletterRepository.save(
                        subscriber
                );

        return newsletterMapper
                .toResponse(
                        updatedSubscriber
                );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @Transactional
    public void delete(
            Long id
    ) {

        NewsletterSubscriber subscriber =
                getSubscriberOrThrow(id);

        newsletterRepository.delete(
                subscriber
        );
    }

    // ==========================================
    // ADMIN : COMPTEURS
    // ==========================================

    public long countAll() {

        return newsletterRepository.count();
    }

    public long countActive() {

        return newsletterRepository
                .countByStatus(
                        NewsletterStatus.ACTIVE
                );
    }

    public long countUnsubscribed() {

        return newsletterRepository
                .countByStatus(
                        NewsletterStatus.UNSUBSCRIBED
                );
    }

    // ==========================================
    // RÉCUPÉRER L'ENTITÉ
    // ==========================================

    private NewsletterSubscriber getSubscriberOrThrow(
            Long id
    ) {

        return newsletterRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Abonné newsletter introuvable."
                                )
                );
    }

    // ==========================================
    // NORMALISATION EMAIL
    // ==========================================

    private String normalizeEmail(
            String email
    ) {

        if (
                email == null
                        || email.isBlank()
        ) {
            return null;
        }

        return email
                .trim()
                .toLowerCase();
    }
}