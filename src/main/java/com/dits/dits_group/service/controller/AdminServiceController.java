package com.dits.dits_group.service.controller;

import com.dits.dits_group.service.entity.Service;
import com.dits.dits_group.service.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/services")
public class AdminServiceController {

    private final ServiceService serviceService;

    public AdminServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(
                serviceService.findAll()
        );
    }

    @PostMapping
    public ResponseEntity<Service> createService(
            @RequestBody Service service
    ) {
        Service savedService = serviceService.save(service);

        return ResponseEntity.ok(savedService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(
            @PathVariable Long id,
            @RequestBody Service updatedService
    ) {
        Service service = serviceService.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Service introuvable")
                );

        service.setTitle(updatedService.getTitle());
        service.setDescription(updatedService.getDescription());
        service.setImageUrl(updatedService.getImageUrl());
        service.setActive(updatedService.isActive());

        return ResponseEntity.ok(
                serviceService.save(service)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Service> changeServiceStatus(
            @PathVariable Long id
    ) {
        Service service = serviceService.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Service introuvable")
                );

        service.setActive(!service.isActive());

        return ResponseEntity.ok(
                serviceService.save(service)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(
            @PathVariable Long id
    ) {
        serviceService.deleteService(id);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message",
                        "Service supprimé avec succès."
                )
        );
    }
}