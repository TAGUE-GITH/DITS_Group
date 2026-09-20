package com.dits.dits_group.service.controller;

import com.dits.dits_group.service.entity.Service;
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
    public ResponseEntity<List<Service>> getActiveServices() {
        return ResponseEntity.ok(
                serviceService.findAllActive()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(
            @PathVariable Long id
    ) {
        Service service = serviceService.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Service introuvable")
                );

        if (!service.isActive()) {
            throw new RuntimeException("Service indisponible");
        }

        return ResponseEntity.ok(service);
    }
}