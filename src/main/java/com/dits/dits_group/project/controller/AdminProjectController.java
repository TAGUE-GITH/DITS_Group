package com.dits.dits_group.project.controller;

import com.dits.dits_group.project.dto.ProjectRequest;
import com.dits.dits_group.project.dto.ProjectResponse;
import com.dits.dits_group.project.service.ProjectService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/projects")
public class AdminProjectController {

    private final ProjectService projectService;

    public AdminProjectController(
            ProjectService projectService
    ) {
        this.projectService =
                projectService;
    }

    // ==========================================
    // TOUTES LES RÉALISATIONS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<ProjectResponse>>
    getAllProjects() {

        return ResponseEntity.ok(
                projectService.findAll()
        );
    }

    // ==========================================
    // DÉTAIL ADMIN
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse>
    getProjectById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                projectService.findById(id)
        );
    }

    // ==========================================
    // CRÉER
    // ==========================================

    @PostMapping
    public ResponseEntity<ProjectResponse>
    createProject(
            @Valid
            @RequestBody ProjectRequest request
    ) {

        ProjectResponse project =
                projectService.create(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(project);
    }

    // ==========================================
    // MODIFIER
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse>
    updateProject(
            @PathVariable Long id,

            @Valid
            @RequestBody ProjectRequest request
    ) {

        return ResponseEntity.ok(
                projectService.update(
                        id,
                        request
                )
        );
    }

    // ==========================================
    // PUBLIER / MASQUER
    // ==========================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectResponse>
    togglePublished(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                projectService
                        .togglePublished(id)
        );
    }

    // ==========================================
    // SUPPRIMER
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>>
    deleteProject(
            @PathVariable Long id
    ) {

        projectService.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Réalisation supprimée avec succès."
                )
        );
    }
}