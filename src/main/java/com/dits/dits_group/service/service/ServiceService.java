package com.dits.dits_group.service.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.common.storage.FileStorageService;
import com.dits.dits_group.service.dto.ServiceRequest;
import com.dits.dits_group.service.dto.ServiceResponse;
import com.dits.dits_group.service.entity.Service;
import com.dits.dits_group.service.mapper.ServiceMapper;
import com.dits.dits_group.service.repository.ServiceRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@org.springframework.stereotype.Service
@Transactional(readOnly = true)
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final FileStorageService fileStorageService;

    public ServiceService(
            ServiceRepository serviceRepository,
            ServiceMapper serviceMapper,
            FileStorageService fileStorageService
    ) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
        this.fileStorageService = fileStorageService;
    }

    public List<ServiceResponse> findAllActive() {
        return serviceRepository.findByActiveTrue().stream().map(serviceMapper::toResponse).toList();
    }

    public List<ServiceResponse> findAll() {
        return serviceRepository.findAll().stream().map(serviceMapper::toResponse).toList();
    }

    public ServiceResponse findById(Long id) {
        return serviceMapper.toResponse(getServiceOrThrow(id));
    }

    @Transactional
    public ServiceResponse create(ServiceRequest request) {
        Service service = serviceMapper.toEntity(request);

        if (hasImage(request.getImage())) {
            service.setImageUrl(fileStorageService.store(request.getImage(), "services"));
        }

        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    @Transactional
    public ServiceResponse update(Long id, ServiceRequest request) {
        Service service = getServiceOrThrow(id);
        serviceMapper.updateEntity(service, request);

        if (hasImage(request.getImage())) {
            fileStorageService.delete(service.getImageUrl());
            service.setImageUrl(fileStorageService.store(request.getImage(), "services"));
        }

        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    @Transactional
    public ServiceResponse toggleActive(Long id) {
        Service service = getServiceOrThrow(id);
        service.setActive(!service.isActive());
        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    @Transactional
    public void delete(Long id) {
        Service service = getServiceOrThrow(id);
        fileStorageService.delete(service.getImageUrl());
        serviceRepository.delete(service);
    }

    private boolean hasImage(MultipartFile image) {
        return image != null && !image.isEmpty();
    }

    private Service getServiceOrThrow(Long id) {
        return serviceRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service introuvable."));
    }
}