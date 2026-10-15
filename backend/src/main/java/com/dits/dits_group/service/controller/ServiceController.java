package com.dits.dits_group.service.controller;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.service.dto.ServiceResponse;
import com.dits.dits_group.service.service.ServiceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getActiveServices() {
        return ResponseEntity.ok(serviceService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        ServiceResponse service = serviceService.findById(id);

        if (!service.isActive()) {
            throw new ResourceNotFoundException("Service indisponible.");
        }

        return ResponseEntity.ok(service);
    }
}