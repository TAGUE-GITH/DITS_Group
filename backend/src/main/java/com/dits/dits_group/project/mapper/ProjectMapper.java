package com.dits.dits_group.project.mapper;

import com.dits.dits_group.project.dto.ProjectRequest;
import com.dits.dits_group.project.dto.ProjectResponse;
import com.dits.dits_group.project.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .clientName(project.getClientName())
                .imageUrl(project.getImageUrl())
                .technologies(project.getTechnologies())
                .completionDate(project.getCompletionDate())
                .published(project.isPublished())
                .build();
    }

    public Project toEntity(ProjectRequest request) {
        return Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .clientName(request.getClientName())
                .technologies(request.getTechnologies())
                .completionDate(request.getCompletionDate())
                .published(request.getPublished() == null || request.getPublished())
                .build();
    }

    public void updateEntity(Project project, ProjectRequest request) {
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setClientName(request.getClientName());
        project.setTechnologies(request.getTechnologies());
        project.setCompletionDate(request.getCompletionDate());

        if (request.getPublished() != null) {
            project.setPublished(request.getPublished());
        }
    }
}