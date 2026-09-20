package com.dits.dits_group.project.service;

import com.dits.dits_group.common.exception.ResourceNotFoundException;
import com.dits.dits_group.project.dto.ProjectRequest;
import com.dits.dits_group.project.dto.ProjectResponse;
import com.dits.dits_group.project.entity.Project;
import com.dits.dits_group.project.mapper.ProjectMapper;
import com.dits.dits_group.project.repository.ProjectRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(
            ProjectRepository projectRepository,
            ProjectMapper projectMapper
    ) {
        this.projectRepository =
                projectRepository;

        this.projectMapper =
                projectMapper;
    }

    // ==========================================
    // PUBLIC : LISTE DES RÉALISATIONS PUBLIÉES
    // ==========================================

    public List<ProjectResponse>
    findAllPublished() {

        return projectRepository
                .findByPublishedTrue()
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    // ==========================================
    // PUBLIC : DÉTAIL D'UNE RÉALISATION
    // ==========================================

    public ProjectResponse
    findPublishedById(Long id) {

        Project project =
                projectRepository
                        .findByIdAndPublishedTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Réalisation introuvable ou indisponible."
                                )
                        );

        return projectMapper.toResponse(
                project
        );
    }

    // ==========================================
    // ADMIN : TOUTES LES RÉALISATIONS
    // ==========================================

    public List<ProjectResponse>
    findAll() {

        return projectRepository
                .findAll()
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    // ==========================================
    // ADMIN : DÉTAIL PAR ID
    // ==========================================

    public ProjectResponse findById(
            Long id
    ) {

        Project project =
                getProjectOrThrow(id);

        return projectMapper.toResponse(
                project
        );
    }

    // ==========================================
    // ADMIN : CRÉER
    // ==========================================

    @Transactional
    public ProjectResponse create(
            ProjectRequest request
    ) {

        Project project =
                projectMapper.toEntity(
                        request
                );

        Project savedProject =
                projectRepository.save(
                        project
                );

        return projectMapper.toResponse(
                savedProject
        );
    }

    // ==========================================
    // ADMIN : MODIFIER
    // ==========================================

    @Transactional
    public ProjectResponse update(
            Long id,
            ProjectRequest request
    ) {

        Project project =
                getProjectOrThrow(id);

        projectMapper.updateEntity(
                project,
                request
        );

        Project updatedProject =
                projectRepository.save(
                        project
                );

        return projectMapper.toResponse(
                updatedProject
        );
    }

    // ==========================================
    // ADMIN : PUBLIER / MASQUER
    // ==========================================

    @Transactional
    public ProjectResponse togglePublished(
            Long id
    ) {

        Project project =
                getProjectOrThrow(id);

        project.setPublished(
                !project.isPublished()
        );

        Project updatedProject =
                projectRepository.save(
                        project
                );

        return projectMapper.toResponse(
                updatedProject
        );
    }

    // ==========================================
    // ADMIN : SUPPRIMER
    // ==========================================

    @Transactional
    public void delete(
            Long id
    ) {

        Project project =
                getProjectOrThrow(id);

        projectRepository.delete(
                project
        );
    }

    // ==========================================
    // MÉTHODE INTERNE
    // ==========================================

    private Project getProjectOrThrow(
            Long id
    ) {

        return projectRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Réalisation introuvable."
                        )
                );
    }
}