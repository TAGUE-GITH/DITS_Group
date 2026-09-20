package com.dits.dits_group.service.service;

import com.dits.dits_group.service.entity.Service;
import com.dits.dits_group.service.repository.ServiceRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> findAllActive() {
        return serviceRepository.findByActiveTrue();
    }

    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public Service save(Service service) {
        return serviceRepository.save(service);
    }

    public void deleteService(Long id) {

        Service service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Service introuvable")
                );

        serviceRepository.delete(service);
    }
}