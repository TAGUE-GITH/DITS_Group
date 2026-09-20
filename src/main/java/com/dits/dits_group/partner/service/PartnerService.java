package com.dits.dits_group.partner.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.partner.dto.PartnerRequest;
import com.dits.dits_group.partner.dto.PartnerResponse;
import com.dits.dits_group.partner.entity.Partner;
import com.dits.dits_group.partner.mapper.PartnerMapper;
import com.dits.dits_group.partner.repository.PartnerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;

    public PartnerService(
            PartnerRepository partnerRepository,
            PartnerMapper partnerMapper
    ) {
        this.partnerRepository =
                partnerRepository;

        this.partnerMapper =
                partnerMapper;
    }

    // ==========================================
    // PUBLIC : LISTE DES PARTENAIRES PUBLIÉS
    // ==========================================

    public List<PartnerResponse> findPublishedPartners() {

        return partnerRepository
                .findByPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(
                        partnerMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // PUBLIC : DÉTAIL D'UN PARTENAIRE PUBLIÉ
    // ==========================================

    public PartnerResponse findPublishedPartnerById(
            Long id
    ) {

        Partner partner =
                partnerRepository
                        .findByIdAndPublishedTrue(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Partenaire introuvable."
                                        )
                        );

        return partnerMapper
                .toResponse(
                        partner
                );
    }

    // ==========================================
    // ADMIN : LISTE COMPLÈTE
    // ==========================================

    public List<PartnerResponse> findAll() {

        return partnerRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(
                        partnerMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : DÉTAIL
    // ==========================================

    public PartnerResponse findById(
            Long id
    ) {

        return partnerMapper
                .toResponse(
                        getPartnerOrThrow(id)
                );
    }

    // ==========================================
    // ADMIN : FILTRE PUBLICATION
    // ==========================================

    public List<PartnerResponse> findByPublished(
            boolean published
    ) {

        return partnerRepository
                .findByPublishedOrderByCreatedAtDesc(
                        published
                )
                .stream()
                .map(
                        partnerMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : RECHERCHE + FILTRE
    // ==========================================

    public List<PartnerResponse> search(
            String query,
            Boolean published
    ) {

        List<Partner> partners;

        if (published != null) {
            partners =
                    partnerRepository
                            .findByPublishedOrderByCreatedAtDesc(
                                    published
                            );
        } else {
            partners =
                    partnerRepository
                            .findAllByOrderByCreatedAtDesc();
        }

        if (
                query == null
                        || query.isBlank()
        ) {
            return partners
                    .stream()
                    .map(
                            partnerMapper::toResponse
                    )
                    .toList();
        }

        String normalizedQuery =
                query
                        .trim()
                        .toLowerCase();

        return partners
                .stream()
                .filter(
                        partner ->
                                containsIgnoreCase(
                                        partner.getName(),
                                        normalizedQuery
                                )
                                        ||
                                        containsIgnoreCase(
                                                partner.getDescription(),
                                                normalizedQuery
                                        )
                )
                .map(
                        partnerMapper::toResponse
                )
                .toList();
    }

    // ==========================================
    // ADMIN : CRÉER
    // ==========================================

    @Transactional
    public PartnerResponse create(
            PartnerRequest request
    ) {

        String normalizedName =
                normalizeName(
                        request.getName()
                );

        if (
                partnerRepository
                        .existsByNameIgnoreCase(
                                normalizedName
                        )
        ) {
            throw new IllegalArgumentException(
                    "Un partenaire avec ce nom existe déjà."
            );
        }

        Partner partner =
                partnerMapper
                        .toEntity(
                                request
                        );

        Partner savedPartner =
                partnerRepository
                        .save(
                                partner
                        );

        return partnerMapper
                .toResponse(
                        savedPartner
                );
    }

    // ==========================================
    // ADMIN : MODIFIER
    // ==========================================

    @Transactional
    public PartnerResponse update(
            Long id,
            PartnerRequest request
    ) {

        Partner partner =
                getPartnerOrThrow(id);

        String normalizedName =
                normalizeName(
                        request.getName()
                );

        Optional<Partner> partnerWithSameName =
                partnerRepository
                        .findByNameIgnoreCase(
                                normalizedName
                        );

        if (
                partnerWithSameName.isPresent()
                        &&
                        !partnerWithSameName
                                .get()
                                .getId()
                                .equals(id)
        ) {
            throw new IllegalArgumentException(
                    "Un partenaire avec ce nom existe déjà."
            );
        }

        partnerMapper.updateEntity(
                partner,
                request
        );

        Partner updatedPartner =
                partnerRepository
                        .save(
                                partner
                        );

        return partnerMapper
                .toResponse(
                        updatedPartner
                );
    }

    // ==========================================
    // ADMIN : PUBLIER / MASQUER
    // ==========================================

    @Transactional
    public PartnerResponse togglePublished(
            Long id
    ) {

        Partner partner =
                getPartnerOrThrow(id);

        partner.setPublished(
                !partner.isPublished()
        );

        Partner updatedPartner =
                partnerRepository
                        .save(
                                partner
                        );

        return partnerMapper
                .toResponse(
                        updatedPartner
                );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @Transactional
    public void delete(
            Long id
    ) {

        Partner partner =
                getPartnerOrThrow(id);

        partnerRepository.delete(
                partner
        );
    }

    // ==========================================
    // COMPTEURS
    // ==========================================

    public long countAll() {

        return partnerRepository.count();
    }

    public long countPublished() {

        return partnerRepository
                .countByPublished(
                        true
                );
    }

    public long countUnpublished() {

        return partnerRepository
                .countByPublished(
                        false
                );
    }

    // ==========================================
    // ENTITY INTERNE
    // ==========================================

    public Partner getPartnerEntityOrThrow(
            Long id
    ) {

        return getPartnerOrThrow(id);
    }

    // ==========================================
    // MÉTHODE PRIVÉE
    // ==========================================

    private Partner getPartnerOrThrow(
            Long id
    ) {

        return partnerRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Partenaire introuvable."
                                )
                );
    }

    // ==========================================
    // NORMALISER LE NOM
    // ==========================================

    private String normalizeName(
            String name
    ) {

        if (
                name == null
                        || name.isBlank()
        ) {
            return null;
        }

        return name.trim();
    }

    // ==========================================
    // RECHERCHE TEXTE
    // ==========================================

    private boolean containsIgnoreCase(
            String value,
            String query
    ) {

        if (
                value == null
                        || value.isBlank()
        ) {
            return false;
        }

        return value
                .toLowerCase()
                .contains(
                        query
                );
    }
}