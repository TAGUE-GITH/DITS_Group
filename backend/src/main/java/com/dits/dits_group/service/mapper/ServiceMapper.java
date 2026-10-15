package com.dits.dits_group.service.mapper;

import com.dits.dits_group.service.dto.ServiceRequest;
import com.dits.dits_group.service.dto.ServiceResponse;
import com.dits.dits_group.service.entity.Service;

import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public ServiceResponse toResponse(Service service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .title(service.getTitle())
                .description(service.getDescription())
                .imageUrl(service.getImageUrl())
                .active(service.isActive())
                .build();
    }

    public Service toEntity(ServiceRequest request) {
        return Service.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .active(request.getActive() == null || request.getActive())
                .build();
    }

    public void updateEntity(Service service, ServiceRequest request) {
        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());

        if (request.getActive() != null) {
            service.setActive(request.getActive());
        }
    }
}