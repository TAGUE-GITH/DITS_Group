package com.dits.dits_group.partner.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.common.storage.FileStorageService;
import com.dits.dits_group.partner.dto.PartnerRequest;
import com.dits.dits_group.partner.dto.PartnerResponse;
import com.dits.dits_group.partner.entity.Partner;
import com.dits.dits_group.partner.mapper.PartnerMapper;
import com.dits.dits_group.partner.repository.PartnerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final FileStorageService fileStorageService;

    public PartnerService(
            PartnerRepository partnerRepository,
            PartnerMapper partnerMapper,
            FileStorageService fileStorageService
    ) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
        this.fileStorageService = fileStorageService;
    }

    public List<PartnerResponse> findPublishedPartners() {
        return partnerRepository
                .findByPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(partnerMapper::toResponse)
                .toList();
    }

    public PartnerResponse findPublishedPartnerById(Long id) {
        Partner partner = partnerRepository
                .findByIdAndPublishedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire introuvable."));

        return partnerMapper.toResponse(partner);
    }

    public List<PartnerResponse> findAll() {
        return partnerRepository.findAllByOrderByCreatedAtDesc().stream().map(partnerMapper::toResponse).toList();
    }

    public PartnerResponse findById(Long id) {
        return partnerMapper.toResponse(getPartnerOrThrow(id));
    }

    public List<PartnerResponse> findByPublished(boolean published) {
        return partnerRepository
                .findByPublishedOrderByCreatedAtDesc(published)
                .stream()
                .map(partnerMapper::toResponse)
                .toList();
    }

    public List<PartnerResponse> search(String query, Boolean published) {
        List<Partner> partners = published != null
                ? partnerRepository.findByPublishedOrderByCreatedAtDesc(published)
                : partnerRepository.findAllByOrderByCreatedAtDesc();

        if (query == null || query.isBlank()) {
            return partners.stream().map(partnerMapper::toResponse).toList();
        }

        String normalizedQuery = query.trim().toLowerCase();

        return partners.stream()
                .filter(partner ->
                        containsIgnoreCase(partner.getName(), normalizedQuery)
                                || containsIgnoreCase(partner.getDescription(), normalizedQuery))
                .map(partnerMapper::toResponse)
                .toList();
    }

    @Transactional
    public PartnerResponse create(PartnerRequest request) {
        String normalizedName = normalizeName(request.getName());

        if (partnerRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new IllegalArgumentException("Un partenaire avec ce nom existe déjà.");
        }

        Partner partner = partnerMapper.toEntity(request);

        if (hasLogo(request.getLogo())) {
            partner.setLogoUrl(fileStorageService.store(request.getLogo(), "partners"));
        }

        return partnerMapper.toResponse(partnerRepository.save(partner));
    }

    @Transactional
    public PartnerResponse update(Long id, PartnerRequest request) {
        Partner partner = getPartnerOrThrow(id);
        String normalizedName = normalizeName(request.getName());

        Optional<Partner> partnerWithSameName = partnerRepository.findByNameIgnoreCase(normalizedName);

        if (partnerWithSameName.isPresent() && !partnerWithSameName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Un partenaire avec ce nom existe déjà.");
        }

        partnerMapper.updateEntity(partner, request);

        if (hasLogo(request.getLogo())) {
            fileStorageService.delete(partner.getLogoUrl());
            partner.setLogoUrl(fileStorageService.store(request.getLogo(), "partners"));
        }

        return partnerMapper.toResponse(partnerRepository.save(partner));
    }

    @Transactional
    public PartnerResponse togglePublished(Long id) {
        Partner partner = getPartnerOrThrow(id);
        partner.setPublished(!partner.isPublished());
        return partnerMapper.toResponse(partnerRepository.save(partner));
    }

    @Transactional
    public void delete(Long id) {
        Partner partner = getPartnerOrThrow(id);
        fileStorageService.delete(partner.getLogoUrl());
        partnerRepository.delete(partner);
    }

    public long countAll() {
        return partnerRepository.count();
    }

    public long countPublished() {
        return partnerRepository.countByPublished(true);
    }

    public long countUnpublished() {
        return partnerRepository.countByPublished(false);
    }

    public Partner getPartnerEntityOrThrow(Long id) {
        return getPartnerOrThrow(id);
    }

    private Partner getPartnerOrThrow(Long id) {
        return partnerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partenaire introuvable."));
    }

    private String normalizeName(String name) {
        return (name == null || name.isBlank()) ? null : name.trim();
    }

    private boolean containsIgnoreCase(String value, String query) {
        return value != null && !value.isBlank() && value.toLowerCase().contains(query);
    }

    private boolean hasLogo(MultipartFile logo) {
        return logo != null && !logo.isEmpty();
    }
}