package com.dits.dits_group.project.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.common.storage.FileStorageService;
import com.dits.dits_group.project.dto.ProjectRequest;
import com.dits.dits_group.project.dto.ProjectResponse;
import com.dits.dits_group.project.entity.Project;
import com.dits.dits_group.project.mapper.ProjectMapper;
import com.dits.dits_group.project.repository.ProjectRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final FileStorageService fileStorageService;

    public ProjectService(
            ProjectRepository projectRepository,
            ProjectMapper projectMapper,
            FileStorageService fileStorageService
    ) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.fileStorageService = fileStorageService;
    }

    public List<ProjectResponse> findAllPublished() {
        return projectRepository.findByPublishedTrue().stream().map(projectMapper::toResponse).toList();
    }

    public ProjectResponse findPublishedById(Long id) {
        Project project = projectRepository
                .findByIdAndPublishedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réalisation introuvable ou indisponible."));

        return projectMapper.toResponse(project);
    }

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream().map(projectMapper::toResponse).toList();
    }

    public ProjectResponse findById(Long id) {
        return projectMapper.toResponse(getProjectOrThrow(id));
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        Project project = projectMapper.toEntity(request);

        if (hasImage(request.getImage())) {
            project.setImageUrl(fileStorageService.store(request.getImage(), "projects"));
        }

        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = getProjectOrThrow(id);
        projectMapper.updateEntity(project, request);

        if (hasImage(request.getImage())) {
            fileStorageService.delete(project.getImageUrl());
            project.setImageUrl(fileStorageService.store(request.getImage(), "projects"));
        }

        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse togglePublished(Long id) {
        Project project = getProjectOrThrow(id);
        project.setPublished(!project.isPublished());
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long id) {
        Project project = getProjectOrThrow(id);
        fileStorageService.delete(project.getImageUrl());
        projectRepository.delete(project);
    }

    private boolean hasImage(MultipartFile image) {
        return image != null && !image.isEmpty();
    }

    private Project getProjectOrThrow(Long id) {
        return projectRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réalisation introuvable."));
    }
}