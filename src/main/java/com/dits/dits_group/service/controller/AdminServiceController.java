package com.dits.dits_group.service.controller;

import com.dits.dits_group.service.dto.ServiceRequest;
import com.dits.dits_group.service.dto.ServiceResponse;
import com.dits.dits_group.service.service.ServiceService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(serviceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.findById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceResponse> createService(@Valid @ModelAttribute ServiceRequest request) {
        ServiceResponse service = serviceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable Long id,
            @Valid @ModelAttribute ServiceRequest request
    ) {
        return ResponseEntity.ok(serviceService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceResponse> changeServiceStatus(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.toggleActive(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteService(@PathVariable Long id) {
        serviceService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Service supprimé avec succès."));
    }
}